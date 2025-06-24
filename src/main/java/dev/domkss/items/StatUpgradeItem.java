package dev.domkss.items;

import dev.domkss.UnderGround;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.List;

public class StatUpgradeItem extends Item implements CustomItem, LootInjectable{

    private static final Identifier identifier = Identifier.of(UnderGround.MOD_ID, "stat_upgrade");
    private static final RegistryKey<ItemGroup> itemGroup = ItemGroups.FUNCTIONAL;


    public StatUpgradeItem() {
        super(new Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier)));
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public RegistryKey<ItemGroup> getItemGroup() {
        return itemGroup;
    }



    public static final RegistryKey<LootTable> DUNGEON =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/simple_dungeon"));

    public static final RegistryKey<LootTable> STRONGHOLD_CORRIDOR =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/stronghold_corridor"));

    public static final RegistryKey<LootTable> STRONGHOLD_CROSSING =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/stronghold_crossing"));

    public static final RegistryKey<LootTable> STRONGHOLD_LIBRARY =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/stronghold_library"));

    public static final RegistryKey<LootTable> MINESHAFT =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/abandoned_mineshaft"));

    public static final RegistryKey<LootTable> JUNGLE_TEMPLE =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/jungle_temple"));

    public static final RegistryKey<LootTable> WOODLAND_MANSION =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/woodland_mansion"));

    public static final RegistryKey<LootTable> ANCIENT_CITY =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/ancient_city"));

    public static final RegistryKey<LootTable> ANCIENT_CITY_ICE_BOX =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/ancient_city_ice_box"));

    public static final RegistryKey<LootTable> PILLAGER_OUTPOST =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/pillager_outpost"));



    @Override
    public List<RegistryKey<LootTable>> getTargetLootTables() {
        return List.of(
                DUNGEON,
                STRONGHOLD_CORRIDOR,
                STRONGHOLD_CROSSING,
                STRONGHOLD_LIBRARY,
                MINESHAFT,
                JUNGLE_TEMPLE,
                WOODLAND_MANSION,
                ANCIENT_CITY,
                ANCIENT_CITY_ICE_BOX,
                PILLAGER_OUTPOST
        );
    }

    @Override
    public float getLootChance() {
        return UnderGround.config.getStatUpgradeSpawnChance();
    }
}
