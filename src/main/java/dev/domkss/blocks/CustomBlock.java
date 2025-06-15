package dev.domkss.blocks;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public interface CustomBlock {
    Identifier getIdentifier();

    RegistryKey<ItemGroup> getItemGroup();
}
