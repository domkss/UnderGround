package dev.domkss.blocks;

import dev.domkss.UnderGround;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;


public class ModBlocks {

    private static final Map<Identifier, Block> BLOCKS = new LinkedHashMap<>();

    public static Block GLOWING_UNBREAKABLE_STONE;

    static {
        GLOWING_UNBREAKABLE_STONE = register(new GlowingUnbreakableStone(Identifier.of(UnderGround.MOD_ID, "glowing_unbreakable_stone")));
    }


    private static Block register(CustomBlock block) {
        BLOCKS.put(block.getIdentifier(), block);
        return block;
    }

    public static void registerAll() {
        for (Map.Entry<Identifier, Block> entry : BLOCKS.entrySet()) {
            Identifier identifier = entry.getKey();
            Block block = entry.getValue();

            Registry.register(Registries.BLOCK, identifier, block);

            Item.Settings itemSettings = new Item.Settings()
                    .useBlockPrefixedTranslationKey()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier));
            Registry.register(Registries.ITEM, identifier, new BlockItem(block, itemSettings));
        }
    }


}
