package dev.domkss.effects;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;

public class RadiationStatusEffect extends StatusEffect {
    public RadiationStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 8889187);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (entity.getHealth() > 1.0F) {
            entity.damage(world, entity.getDamageSources().magic(), 1.0F);
        }

        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        amplifier=amplifier-1;
        if(amplifier<0) return false;
        int i = 30 >> amplifier;
        return i == 0 || duration % i == 0;
    }
}
