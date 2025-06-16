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
            if (UnderGround.config.getReplaceUndergroundOceansWithRadioactiveWater()) {
                cir.setReturnValue(ModBlocks.RADIOACTIVE_WATER_BLOCK.getDefaultState());
            }

        }
    }

}
