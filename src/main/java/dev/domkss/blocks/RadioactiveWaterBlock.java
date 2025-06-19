package dev.domkss.blocks;

import dev.domkss.UnderGround;
import dev.domkss.blocks.fluids.ModFluids;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class RadioactiveWaterBlock extends FluidBlock implements CustomBlock {

    private static final Identifier identifier = Identifier.of(UnderGround.MOD_ID, "radioactive_water_block");

    private static final RegistryKey<ItemGroup> itemGroup = null;

    public RadioactiveWaterBlock() {
        super(ModFluids.STILL_RADIOACTIVE_WATER,
                        AbstractBlock.Settings.copy(Blocks.WATER).luminance(state->5)
                                .registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier)).liquid()
                );

    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public RegistryKey<ItemGroup> getItemGroup() {
        return itemGroup;
    }
}
