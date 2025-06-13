package dev.domkss.mixin;

import dev.domkss.UnderGround;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {


    @Shadow public abstract ServerWorld getOverworld();

    @Inject(method = "setupSpawn", at = @At("TAIL"))
    private static void onSetupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo ci) {
        if(!debugWorld){
            BlockPos spawnPos = UnderGround.config.getSpawnPos();
            worldProperties.setSpawnPos(spawnPos, 0.0F);
            UnderGround.LOGGER.info("[UnderGroundMod] Spawn position modified: {}", spawnPos);
        }

    }

    @Inject(method = "prepareStartRegion", at = @At("TAIL"))
    private void onPrepareStartRegion(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        ServerWorld serverWorld = this.getOverworld();
        BlockPos blockPos = serverWorld.getSpawnPos();
        UnderGround.LOGGER.info("[UnderGroundMod] Spawn position read: {}", blockPos);

    }


}