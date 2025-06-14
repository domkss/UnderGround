package dev.domkss.mixin.worldgen;

import dev.domkss.UnderGround;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {


    @Shadow
    public abstract ServerWorld getOverworld();

    @Inject(method = "setupSpawn", at = @At("TAIL"))
    private static void onSetupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo ci) {
        if (!debugWorld) {
            BlockPos spawnPos = UnderGround.config.getSpawnPos();
            worldProperties.setSpawnPos(spawnPos, 0.0F);
            UnderGround.LOGGER.info("[UnderGroundMod] Spawn position modified: {}", spawnPos);
        }

    }

    @Inject(method = "prepareStartRegion", at = @At("TAIL"))
    private void afterPrepareStartRegion(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        ServerWorld serverWorld = this.getOverworld();
        BlockPos spawn = serverWorld.getSpawnPos();
        createStarterChest(serverWorld, spawn);
    }


    @Unique
    private void createStarterChest(ServerWorld serverWorld, BlockPos spawn) {

        //Todo: Prevent respawning and refilling

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
        }

        UnderGround.LOGGER.info("Starter chest placed in prepareStartRegion at {}", chestPos);

    }


}