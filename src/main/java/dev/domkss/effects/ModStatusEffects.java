package dev.domkss.effects;

import dev.domkss.UnderGround;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModStatusEffects {

    public static final RegistryEntry<StatusEffect> RADIATION_EFFECT = Registry.registerReference(
            Registries.STATUS_EFFECT,
            Identifier.of(UnderGround.MOD_ID, "radiation_effect"),
            new RadiationStatusEffect()
    );

    public static void registerAll(){

    }

}
