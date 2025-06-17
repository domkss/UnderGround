package dev.domkss.mixin.worldgen;

import dev.domkss.UnderGround;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "setupSpawn", at = @At("TAIL"))
    private static void onSetupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo ci) {
        if (!debugWorld) {
            BlockPos spawnPos = UnderGround.config.getSpawnPos();
            worldProperties.setSpawnPos(spawnPos, 0.0F);

            createStarterChest(world,spawnPos);
            UnderGround.LOGGER.info("Spawn position modified: " + spawnPos);
        }

    }

    @Unique
    private static void createStarterChest(ServerWorld serverWorld, BlockPos spawn) {
        int spawnRadiusY = UnderGround.config.getSpawnRadiusY();
        BlockPos chestPos = new BlockPos(
                spawn.getX() + 5,
                spawn.getY() - spawnRadiusY + 1,
                spawn.getZ() + 5
        );
        serverWorld.setBlockState(chestPos, Blocks.CHEST.getDefaultState());

        BlockEntity be = serverWorld.getBlockEntity(chestPos);
        if (be instanceof ChestBlockEntity chest) {
            chest.setStack(0, new ItemStack(Items.WOODEN_PICKAXE));
            chest.setStack(1, new ItemStack(Items.WOODEN_PICKAXE));
            chest.setStack(2, new ItemStack(Items.WOODEN_PICKAXE));
            chest.setStack(3, new ItemStack(Items.WOODEN_PICKAXE));
            chest.setStack(4, new ItemStack(Items.WOODEN_PICKAXE));
            chest.setStack(5, new ItemStack(Items.WOODEN_PICKAXE));
        }

        UnderGround.LOGGER.info("Starter chest placed in prepareStartRegion at " + chestPos);

    }


}