package dev.domkss.blocks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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
import java.util.function.Supplier;


public class ModBlocks {

    private static final Map<Identifier, CustomBlock> BLOCKS = new LinkedHashMap<>();

    public static Block GLOWING_UNBREAKABLE_STONE;

    public static Block RADIOACTIVE_WATER_BLOCK;

    static {
        GLOWING_UNBREAKABLE_STONE = register(GlowingUnbreakableStone::new);
        RADIOACTIVE_WATER_BLOCK = register(RadioactiveWaterBlock::new);
    }


    private static <T extends Block & CustomBlock> Block register(Supplier<T> supplier) {
        CustomBlock block = supplier.get();
        BLOCKS.put(block.getIdentifier(), block);
        return (Block) block;
    }

    public static void registerAll() {
        for (Map.Entry<Identifier, CustomBlock> entry : BLOCKS.entrySet()) {
            Identifier identifier = entry.getKey();
            CustomBlock customBlock = entry.getValue();
            Block block = (Block) customBlock;

            //Register the block
            Registry.register(Registries.BLOCK, identifier, block);


            if (customBlock.getItemGroup() != null) {
                //Add the block as an item
                Item.Settings itemSettings = new Item.Settings()
                        .useBlockPrefixedTranslationKey()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier));
                Registry.register(Registries.ITEM, identifier, new BlockItem(block, itemSettings));
                //Add the item to an item group
                ItemGroupEvents.modifyEntriesEvent(customBlock.getItemGroup()).register(content -> {
                    content.add(block);
                });
            }

        }
    }


}
