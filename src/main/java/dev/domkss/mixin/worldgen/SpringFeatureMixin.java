package dev.domkss.mixin.worldgen;

import dev.domkss.UnderGround;
import dev.domkss.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.gen.feature.SpringFeature;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpringFeature.class)
public abstract class SpringFeatureMixin {


    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    private void onGenerate(FeatureContext<SpringFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        SpringFeatureConfig oldConfig = context.getConfig();

        if (oldConfig.state.getBlockState().getFluidState().isOf(Fluids.WATER)) {

            if (context.getRandom().nextDouble() < UnderGround.config.getRadioactiveWaterSpringChance()) {
                BlockState newState = ModBlocks.RADIOACTIVE_WATER_BLOCK.getDefaultState();

                if (newState != oldConfig.state.getBlockState()) {
                    FeatureContext<SpringFeatureConfig> newContext = getSpringFeatureConfigFeatureContext(context, newState, oldConfig);

                    // Cancel original generate call and invoke with new context
                    boolean result = ((SpringFeature) (Object) this).generate(newContext);
                    cir.setReturnValue(result);
                    cir.cancel();
                }
            }
        }
    }

    @Unique
    private static @NotNull FeatureContext<SpringFeatureConfig> getSpringFeatureConfigFeatureContext(FeatureContext<SpringFeatureConfig> context, BlockState newState, SpringFeatureConfig oldConfig) {
        SpringFeatureConfig newConfig = new SpringFeatureConfig(
                newState.getFluidState(),
                oldConfig.requiresBlockBelow,
                oldConfig.rockCount,
                oldConfig.holeCount,
                oldConfig.validBlocks
        );

        FeatureContext<SpringFeatureConfig> newContext = new FeatureContext<>(
                context.getFeature(),
                context.getWorld(),
                context.getGenerator(),
                context.getRandom(),
                context.getOrigin(),
                newConfig
        );
        return newContext;
    }
}

