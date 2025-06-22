package dev.domkss.blocks.fluids;

import dev.domkss.UnderGround;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModFluids {

    public static final FlowableFluid STILL_RADIOACTIVE_WATER = Registry.register(Registries.FLUID, Identifier.of(UnderGround.MOD_ID, "radioactive_water"), new RadioactiveWaterFluid.Still());
    public static final FlowableFluid FLOWING_RADIOACTIVE_WATER = Registry.register(Registries.FLUID, Identifier.of(UnderGround.MOD_ID, "flowing_radioactive_water"), new RadioactiveWaterFluid.Flowing());

}
