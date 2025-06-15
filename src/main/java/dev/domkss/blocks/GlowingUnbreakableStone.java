package dev.domkss.blocks;

import dev.domkss.UnderGround;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;


public class GlowingUnbreakableStone extends Block implements CustomBlock {

    private static final Identifier identifier = Identifier.of(UnderGround.MOD_ID, "glowing_unbreakable_stone");
    private static final RegistryKey<ItemGroup> itemGroup = ItemGroups.BUILDING_BLOCKS;


    public GlowingUnbreakableStone() {
        super(AbstractBlock.Settings
                .create()
                .mapColor(MapColor.STONE_GRAY)
                .registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier))
                .luminance(state -> 15)// Light level 15
                .strength(-1.0F, 3600000.0F)// Unbreakable
                .requiresTool()
                .dropsNothing());
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
