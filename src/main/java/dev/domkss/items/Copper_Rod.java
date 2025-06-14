package dev.domkss.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;



public class Copper_Rod extends CustomItem {

    public Copper_Rod(Identifier identifier) {
        super(identifier,
                ItemGroups.BUILDING_BLOCKS,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier)));
    }

}
