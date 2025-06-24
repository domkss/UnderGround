package dev.domkss.items;

import dev.domkss.UnderGround;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CopperRodItem extends Item implements CustomItem{

    private static final Identifier identifier = Identifier.of(UnderGround.MOD_ID, "copper_rod");
    private static final RegistryKey<ItemGroup> itemGroup = ItemGroups.INGREDIENTS;


    public CopperRodItem() {
        super(new Item.Settings()
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
}
