package dev.domkss.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
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
    public static Item STAT_UPGRADE;

    static {
        COPPER_ROD = register(Copper_Rod::new);
        RADIOACTIVE_WATER_BUCKET = register(Radioactive_Water_Bucket::new);
        STAT_UPGRADE = register(Stat_Upgrade::new);
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

        //Register Loot Tables
        LootTableEvents.MODIFY.register((lootTableRegistryKey, tableBuilder, source, registries) -> {

            for (Item item : ITEMS.values()) {
                if (item instanceof LootInjectable li && li.getTargetLootTables().contains(lootTableRegistryKey)) {
                    int weight = Math.round(li.getLootChance() *  10000f);
                    int emptyWeight =  10000 - weight;

                    LootPool pool = LootPool.builder()
                            .with(ItemEntry.builder((Item) li).weight(weight))
                            .with(EmptyEntry.builder().weight(emptyWeight))
                            .rolls(ConstantLootNumberProvider.create(1))
                            .build();

                    tableBuilder.pool(pool);
                }

            }

        });


    }

}
