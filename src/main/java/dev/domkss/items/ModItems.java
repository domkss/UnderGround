package dev.domkss.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {

    private static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();

    public static Item COPPER_ROD;
    public static Item RADIOACTIVE_WATER_BUCKET;

    static {
        COPPER_ROD = register(Copper_Rod::new);
        RADIOACTIVE_WATER_BUCKET = register(Radioactive_Water_Bucket::new);
    }


    private static <T extends Item & CustomItem> Item register(Supplier<T> supplier) {
        Item item = supplier.get();
        ITEMS.put(((CustomItem) item).getIdentifier(), item);
        return item;
    }

    public static void registerAll() {
        for (Map.Entry<Identifier, Item> entry : ITEMS.entrySet()) {
            Identifier identifier = entry.getKey();
            Item item = entry.getValue();

            //Register the custom item
            Registry.register(Registries.ITEM, identifier, item);
            //Add the item to an item group
            ItemGroupEvents.modifyEntriesEvent(((CustomItem) item).getItemGroup()).register(content -> {
                content.add(item);
            });
        }
    }

}
