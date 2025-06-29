package dev.domkss.items;

import dev.domkss.UnderGround;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;

public class MiningCoreUpgradeItem extends Item implements CustomItem, LootInjectable{

    private static final Identifier identifier = Identifier.of(UnderGround.MOD_ID, "mining_core_upgrade_smithing_template");
    private static final RegistryKey<ItemGroup> itemGroup = ItemGroups.INGREDIENTS;


    public MiningCoreUpgradeItem() {
        super(new Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier)).rarity(Rarity.EPIC));
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public RegistryKey<ItemGroup> getItemGroup() {
        return itemGroup;
    }




    public static final RegistryKey<LootTable> STRONGHOLD_CORRIDOR =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/stronghold_corridor"));

    public static final RegistryKey<LootTable> STRONGHOLD_CROSSING =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/stronghold_crossing"));

    public static final RegistryKey<LootTable> STRONGHOLD_LIBRARY =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/stronghold_library"));

    public static final RegistryKey<LootTable> WOODLAND_MANSION =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/woodland_mansion"));

    public static final RegistryKey<LootTable> ANCIENT_CITY =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/ancient_city"));

    public static final RegistryKey<LootTable> ANCIENT_CITY_ICE_BOX =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/ancient_city_ice_box"));




    @Override
    public List<RegistryKey<LootTable>> getTargetLootTables() {
        return List.of(
                STRONGHOLD_CORRIDOR,
                STRONGHOLD_CROSSING,
                STRONGHOLD_LIBRARY,
                WOODLAND_MANSION,
                ANCIENT_CITY,
                ANCIENT_CITY_ICE_BOX
        );
    }

    @Override
    public float getLootChance() {
        return UnderGround.config.getMiningCoreUpgradeSpawnChance();
    }
}
