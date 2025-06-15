package dev.domkss.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public interface CustomItem {
    Identifier getIdentifier();

    RegistryKey<ItemGroup> getItemGroup();
}
