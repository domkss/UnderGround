package dev.domkss.blocks;

import dev.domkss.UnderGround;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;


public class GlowingUnbreakableStone extends CustomBlock {

    public GlowingUnbreakableStone(Identifier identifier) {
        super(identifier,
                AbstractBlock.Settings
                .create()
                .mapColor(MapColor.STONE_GRAY)
                .registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier))
                .luminance(state -> 15)// Light level 15
                .strength(-1.0F, 3600000.0F)// Unbreakable
                .requiresTool()
                .dropsNothing());
    }

}
