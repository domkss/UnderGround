package dev.domkss.mixin.entity;

import dev.domkss.blocks.fluids.ModFluids;
import dev.domkss.persistance.GenericWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity {
    @Shadow
    public abstract ServerWorld getServerWorld();

    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickRadioactiveFluid(CallbackInfo ci) {
        if (this.getWorld().isClient) return;

        BlockPos pos = this.getBlockPos();
        FluidState fluid = this.getWorld().getFluidState(pos);
        GenericWorldData persistentData = GenericWorldData.get(this.getServerWorld());

        if (fluid.isOf(ModFluids.STILL_RADIOACTIVE_WATER) || fluid.isOf(ModFluids.FLOWING_RADIOACTIVE_WATER)) {

            Integer exposureTime = (Integer) persistentData.getDataByKey(this.getSavedEntityId() + "_radioactive_timer");
            if (exposureTime == null) exposureTime = 0;
            else exposureTime++;

            persistentData.saveData(new Pair<>(this.getSavedEntityId() + "_radioactive_timer", exposureTime));

            if (exposureTime >= 60) {
                LivingEntity player = (LivingEntity) (Object) this;
                if (!player.hasStatusEffect(StatusEffects.POISON)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
                }
            }
        } else {
            // Reset when no longer in fluid
            persistentData.saveData(new Pair<>(this.getSavedEntityId() + "_radioactive_timer", 0));
        }
    }
}
