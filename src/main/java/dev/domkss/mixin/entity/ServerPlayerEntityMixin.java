package dev.domkss.mixin.entity;

import com.mojang.datafixers.util.Pair;
import dev.domkss.UnderGround;
import dev.domkss.blocks.fluids.ModFluids;
import dev.domkss.persistance.PersistentWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
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
        PersistentWorldData persistentData = PersistentWorldData.get(this.getServerWorld());
        Integer exposureTime = (Integer) persistentData.getDataByKey(this.getSavedEntityId() + "_radioactive_timer");
        if (exposureTime == null) exposureTime = 1;

        if (fluid.isOf(ModFluids.STILL_RADIOACTIVE_WATER) || fluid.isOf(ModFluids.FLOWING_RADIOACTIVE_WATER)) {

            if (exposureTime >= UnderGround.config.getMaxTickNumberOfRadioactiveWaterExposure()) {
                LivingEntity player = (LivingEntity) (Object) this;
                if (!player.hasStatusEffect(StatusEffects.POISON)) {

                    //Kill the player if on half hearth
                    if (player.getHealth() <= 1.0F) {
                        DynamicRegistryManager registryManager = this.getServerWorld().getRegistryManager();
                        RegistryEntry.Reference<DamageType> radiationDamageType =
                                registryManager.getOptional(RegistryKeys.DAMAGE_TYPE)
                                        .orElseThrow().getEntry(Identifier.of("underground", "radiation"))
                                        .orElseThrow();
                        DamageSource damageSource = new DamageSource(radiationDamageType);
                        player.damage(this.getServerWorld(), damageSource, 1000.0F);
                    } else {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 0));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,200,0));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,200,0));
                    }

                }
            }

            if (exposureTime + 1 < Integer.MAX_VALUE) {
                exposureTime++;
                persistentData.saveData(new Pair<>(this.getSavedEntityId() + "_radioactive_timer", exposureTime));
            }
        } else {
            // Decrease counter when no longer in radioactive water
            if (exposureTime > 0) {
                exposureTime--;
                persistentData.saveData(new Pair<>(this.getSavedEntityId() + "_radioactive_timer", exposureTime));
            }
        }

    }
}
