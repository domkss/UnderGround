package dev.domkss.networking;

import com.mojang.datafixers.util.Pair;
import dev.domkss.UnderGround;
import dev.domkss.items.ModItems;
import dev.domkss.networking.payloads.RequestSkillsDataIncreasePayload;
import dev.domkss.networking.payloads.RequestSkillsDataPayload;
import dev.domkss.networking.payloads.SkillsDataPayload;
import dev.domkss.persistance.PlayerStatManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public class ServerNetworkHandler {

    public static void register() {
        PayloadTypeRegistry.playS2C().register(SkillsDataPayload.ID, SkillsDataPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestSkillsDataPayload.ID, RequestSkillsDataPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestSkillsDataIncreasePayload.ID, RequestSkillsDataIncreasePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RequestSkillsDataIncreasePayload.ID, RequestSkillsDataIncreasePayload.CODEC);

        PacketHandler.registerGlobalServerReceiver(RequestSkillsDataPayload.ID, ((player, payload) ->
        {

            PlayerStatManager playerStatManager = new PlayerStatManager(player);

            int health = playerStatManager.getStat(PlayerStatManager.StatType.HEALTH);
            int armor = playerStatManager.getStat(PlayerStatManager.StatType.ARMOR);
            int speed = playerStatManager.getStat(PlayerStatManager.StatType.SPEED);
            int haste = playerStatManager.getStat(PlayerStatManager.StatType.HASTE);
            int radiation_resistance = playerStatManager.getStat(PlayerStatManager.StatType.RADIATION_RESISTANCE);


            PacketHandler.sendToClient(player, new SkillsDataPayload(
                    new SkillsDataPayload.SkillsData(
                            Pair.of(health, UnderGround.config.getMaxBonusHealth()),
                            Pair.of(armor, UnderGround.config.getMaxBonusArmor()),
                            Pair.of(speed, UnderGround.config.getMaxBonusSpeed()),
                            Pair.of(haste, UnderGround.config.getMaxBonusHaste()),
                            Pair.of(radiation_resistance, UnderGround.config.getMaxBonusRadiationResistance()))));
        }
        ));

        PacketHandler.registerGlobalServerReceiver(RequestSkillsDataIncreasePayload.ID, ((player, payload) -> {

            String statKeyToIncrease = payload.statKey().toLowerCase();

            PlayerStatManager playerStatManager = new PlayerStatManager(player);

            int currentStat= playerStatManager.getStatByKey(statKeyToIncrease);

            if (statIncreaseIsPossible(statKeyToIncrease, currentStat) && consumeEmerald(player, 1)) {
                currentStat++;
                playerStatManager.setStatByKey(statKeyToIncrease,currentStat);

                //Response if the stat increase was successful
                PacketHandler.sendToClient(player, new RequestSkillsDataIncreasePayload(statKeyToIncrease));
            }else{
                player.sendMessage(Text.literal("1 stat upgrade item is required to increase " + statKeyToIncrease).formatted(Formatting.RED), false);
            }

        }));


    }

    private static boolean statIncreaseIsPossible(String statKeyToIncrease, int currentStat) {
        return switch (statKeyToIncrease.toLowerCase()) {
            case "health" -> currentStat < UnderGround.config.getMaxBonusHealth();
            case "armor" -> currentStat < UnderGround.config.getMaxBonusArmor();
            case "speed" -> currentStat < UnderGround.config.getMaxBonusSpeed();
            case "haste" -> currentStat < UnderGround.config.getMaxBonusHaste();
            case "resistance" -> currentStat<UnderGround.config.getMaxBonusRadiationResistance();
            default -> false;
        };
    }

    private static boolean consumeEmerald(ServerPlayerEntity player, int amount) {
        PlayerInventory inv = player.getInventory();
        int emeraldsFound = 0;

        // Count emeralds
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.getItem() == ModItems.STAT_UPGRADE) {
                emeraldsFound += stack.getCount();
            }
        }

        if (emeraldsFound < amount) {
            return false;
        }

        // Remove emeralds
        int toRemove = amount;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.getItem() == ModItems.STAT_UPGRADE) {
                int remove = Math.min(stack.getCount(), toRemove);
                stack.decrement(remove);
                toRemove -= remove;
                if (toRemove <= 0) break;
            }
        }

        return true;
    }

}
