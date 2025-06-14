package dev.domkss.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomItem extends Item {

    protected Identifier identifier;
    protected RegistryKey <ItemGroup> itemGroup;


    public CustomItem(Identifier identifier, RegistryKey <ItemGroup> itemGroup,Item.Settings settings) {
        super(settings);
        this.itemGroup=itemGroup;
        this.identifier=identifier;
    }

    protected Identifier getIdentifier(){
        return identifier;
    }

    protected RegistryKey <ItemGroup> getItemGroup (){
        return itemGroup;
    }

}
