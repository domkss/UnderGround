package dev.domkss.mixin.entity;

import com.mojang.datafixers.util.Pair;
import dev.domkss.UnderGround;
import dev.domkss.blocks.fluids.ModFluids;
import dev.domkss.effects.ModStatusEffects;
import dev.domkss.persistance.PersistentWorldData;
import dev.domkss.persistance.PlayerStatManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity {

    @Unique
    private static final Identifier HEALTH_MODIFIER_ID = Identifier.of(UnderGround.MOD_ID, "health_modifier");
    @Unique
    private static final Identifier ARMOR_MODIFIER_ID = Identifier.of(UnderGround.MOD_ID, "armor_modifier");
    @Unique
    private static final Identifier SPEED_MODIFIER_ID = Identifier.of(UnderGround.MOD_ID, "speed_modifier");
    @Unique
    private static final Identifier HASTE_MODIFIER_ID = Identifier.of(UnderGround.MOD_ID, "haste_modifier");

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

            LivingEntity player = (LivingEntity) (Object) this;
            Integer maxExposureTime = UnderGround.config.getMaxTickNumberOfRadioactiveWaterExposure();

            if (exposureTime >= maxExposureTime) {

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
                        if(!player.hasStatusEffect(ModStatusEffects.RADIATION_EFFECT)) {
                          if(exposureTime>maxExposureTime*12){
                              player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.RADIATION_EFFECT, 200, 4));
                          }else if(exposureTime>maxExposureTime*6){
                              player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.RADIATION_EFFECT, 200, 3));
                          }else if(exposureTime>maxExposureTime*3){
                              player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.RADIATION_EFFECT, 200, 2));
                          }else {
                              player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.RADIATION_EFFECT, 200, 1));
                          }
                        }

                        if(!player.hasStatusEffect(StatusEffects.SLOWNESS))
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 0));
                        if(!player.hasStatusEffect(StatusEffects.NAUSEA))
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
                    }

            }else{
                if(!player.hasStatusEffect(ModStatusEffects.RADIATION_EFFECT))
                    player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.RADIATION_EFFECT,200,0));
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

    @Inject(method = "tick", at = @At("HEAD"))
    private void applyStatBonuses(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        PlayerStatManager playerStatManager = new PlayerStatManager(player);


        // --- HEALTH ---
        EntityAttributeInstance maxHealthAttr = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.removeModifier(HEALTH_MODIFIER_ID);

            maxHealthAttr.addPersistentModifier(
                    new EntityAttributeModifier(HEALTH_MODIFIER_ID,
                            playerStatManager.getStat(PlayerStatManager.StatType.HEALTH),
                            EntityAttributeModifier.Operation.ADD_VALUE));
        }

        // --- ARMOR ---
        EntityAttributeInstance armorAttr = player.getAttributeInstance(EntityAttributes.ARMOR);
        if (armorAttr != null) {
            armorAttr.removeModifier(ARMOR_MODIFIER_ID);

            armorAttr.addPersistentModifier(
                    new EntityAttributeModifier(ARMOR_MODIFIER_ID,
                            playerStatManager.getStat(PlayerStatManager.StatType.ARMOR),
                            EntityAttributeModifier.Operation.ADD_VALUE));
        }

        // --- SPEED ---
        EntityAttributeInstance speedAttr = player.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
        if (speedAttr != null) {
            speedAttr.removeModifier(SPEED_MODIFIER_ID);
            double percentBonus = 0.03 * playerStatManager.getStat(PlayerStatManager.StatType.SPEED); // +3% per level
            speedAttr.addPersistentModifier(
                    new EntityAttributeModifier(SPEED_MODIFIER_ID,
                            percentBonus,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }

        // --- HASTE ---
        EntityAttributeInstance hasteAttr = player.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED);
        if (hasteAttr != null) {
            hasteAttr.removeModifier(HASTE_MODIFIER_ID);
            double percentBonus = 0.04 * playerStatManager.getStat(PlayerStatManager.StatType.HASTE); // +5% per level
            hasteAttr.addPersistentModifier(
                    new EntityAttributeModifier(HASTE_MODIFIER_ID,
                            percentBonus,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }


}
