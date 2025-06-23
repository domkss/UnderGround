package dev.domkss.items;

import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;

import java.util.List;

public interface LootInjectable {

    List<RegistryKey<LootTable>> getTargetLootTables();

    float getLootChance();

}
