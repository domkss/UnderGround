package dev.domkss.items;

import dev.domkss.UnderGround;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {

    private static final Map<Identifier, CustomItem> ITEMS = new LinkedHashMap<>();

    public static Item COPPER_ROD;

    static {
        COPPER_ROD = register(new Copper_Rod(Identifier.of(UnderGround.MOD_ID, "copper_rod")));
    }


    private static Item register(CustomItem item) {
        ITEMS.put(item.getIdentifier(), item);
        return item;
    }

    public static void registerAll() {
        for (Map.Entry<Identifier, CustomItem> entry : ITEMS.entrySet()) {
            Identifier identifier = entry.getKey();
            CustomItem item = entry.getValue();

            //Register the custom item
            Registry.register(Registries.ITEM, identifier, item);
            //Add the item to an item group
            ItemGroupEvents.modifyEntriesEvent(item.itemGroup).register(content -> {
                content.add(item);
            });
        }
    }

}
