package dev.domkss.mixin.worldgen;

import dev.domkss.UnderGround;
import dev.domkss.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AquiferSampler.Impl.class)
public abstract class AquiferSamplerMixin {

    @Inject(
            method = "Lnet/minecraft/world/gen/chunk/AquiferSampler$Impl;apply(Lnet/minecraft/world/gen/densityfunction/DensityFunction$NoisePos;D)Lnet/minecraft/block/BlockState;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void onApply(
            DensityFunction.NoisePos pos, double density,
            CallbackInfoReturnable<BlockState> cir
    ) {
        BlockState result = cir.getReturnValue();

        if (result != null && result.getFluidState() != null && result.getFluidState().isOf(Fluids.WATER)) {
            double chance = positionRandomChance(pos.blockX(), pos.blockY(), pos.blockZ());
            if (chance < UnderGround.config.getRadioactiveWaterChance()) {
                cir.setReturnValue(ModBlocks.RADIOACTIVE_WATER_BLOCK.getDefaultState());
            }

        }
    }

    @Unique
    private static double positionRandomChance(int x, int y, int z) {
        long seed = (((long) x * 3129871L) ^ ((long) y * 116129781L) ^ ((long) z * 132897987541L)) & 0x7FFFFFFFFFFFFFFFL;
        return (double) (seed % 10000) / 10000.0;
    }


}
