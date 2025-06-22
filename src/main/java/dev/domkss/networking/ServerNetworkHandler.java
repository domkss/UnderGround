package dev.domkss.networking;

import com.mojang.datafixers.util.Pair;
import dev.domkss.UnderGround;
import dev.domkss.networking.payloads.RequestSkillsDataIncreasePayload;
import dev.domkss.networking.payloads.RequestSkillsDataPayload;
import dev.domkss.networking.payloads.SkillsDataPayload;
import dev.domkss.persistance.PersistentWorldData;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.Optional;

public class ServerNetworkHandler {

    public static void register() {
        PayloadTypeRegistry.playS2C().register(SkillsDataPayload.ID, SkillsDataPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestSkillsDataPayload.ID, RequestSkillsDataPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestSkillsDataIncreasePayload.ID, RequestSkillsDataIncreasePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RequestSkillsDataIncreasePayload.ID, RequestSkillsDataIncreasePayload.CODEC);

        PacketHandler.registerGlobalServerReceiver(RequestSkillsDataPayload.ID, ((player, payload) ->
        {
            PersistentWorldData persistentWorldData = PersistentWorldData.get(Objects.requireNonNull(player.getServer()).getOverworld());

            int health = (int) Optional.ofNullable(persistentWorldData.getDataByKey(player.getUuidAsString() + "_player_stats_bonus_health")).orElse(0);
            int armor = (int) Optional.ofNullable(persistentWorldData.getDataByKey(player.getUuidAsString() + "_player_stats_bonus_armor")).orElse(0);
            int speed = (int) Optional.ofNullable(persistentWorldData.getDataByKey(player.getUuidAsString() + "_player_stats_bonus_speed")).orElse(0);
            int haste = (int) Optional.ofNullable(persistentWorldData.getDataByKey(player.getUuidAsString() + "_player_stats_bonus_haste")).orElse(0);


            PacketHandler.sendToClient(player, new SkillsDataPayload(
                    new SkillsDataPayload.SkillsData(
                            Pair.of(health, UnderGround.config.getMaxBonusHealth()),
                            Pair.of(armor, UnderGround.config.getMaxBonusArmor()),
                            Pair.of(speed, UnderGround.config.getMaxBonusSpeed()),
                            Pair.of(haste, UnderGround.config.getMaxBonusHaste()))));
        }
        ));

        PacketHandler.registerGlobalServerReceiver(RequestSkillsDataIncreasePayload.ID, ((player, payload) -> {

            PersistentWorldData persistentWorldData = PersistentWorldData.get(Objects.requireNonNull(player.getServer()).getOverworld());
            String statKeyToIncrease = payload.statKey().toLowerCase();
            Integer currentStat = (Integer) persistentWorldData.getDataByKey(player.getUuidAsString() + "_player_stats_bonus_" + statKeyToIncrease);
            currentStat = currentStat != null ? currentStat : 0;

            if (statIncreaseIsPossible(statKeyToIncrease, currentStat) && consumeEmerald(player, 1)) {
                currentStat++;
                persistentWorldData.saveData(new Pair<>(player.getUuidAsString() + "_player_stats_bonus_" + statKeyToIncrease, currentStat));
                //Response if the stat increase was successful
                PacketHandler.sendToClient(player, new RequestSkillsDataIncreasePayload(statKeyToIncrease));
            }else{
                player.sendMessage(Text.literal("1 emerald is required to increase " + statKeyToIncrease).formatted(Formatting.RED), false);
            }

        }));


    }

    private static boolean statIncreaseIsPossible(String statKeyToIncrease, int currentStat) {
        return switch (statKeyToIncrease.toLowerCase()) {
            case "health" -> currentStat < UnderGround.config.getMaxBonusHealth();
            case "armor" -> currentStat < UnderGround.config.getMaxBonusArmor();
            case "speed" -> currentStat < UnderGround.config.getMaxBonusSpeed();
            case "haste" -> currentStat < UnderGround.config.getMaxBonusHaste();
            default -> false;
        };
    }

    private static boolean consumeEmerald(ServerPlayerEntity player, int amount) {
        PlayerInventory inv = player.getInventory();
        int emeraldsFound = 0;

        // Count emeralds
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.getItem() == Items.EMERALD) {
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
            if (stack.getItem() == Items.EMERALD) {
                int remove = Math.min(stack.getCount(), toRemove);
                stack.decrement(remove);
                toRemove -= remove;
                if (toRemove <= 0) break;
            }
        }

        return true;
    }

}
