package dev.domkss.items;

import dev.domkss.UnderGround;
import dev.domkss.blocks.fluids.ModFluids;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class RadioactiveWaterBucketItem extends BucketItem implements CustomItem {

    private static final Identifier identifier = Identifier.of(UnderGround.MOD_ID, "radioactive_water_bucket");
    private static final RegistryKey<ItemGroup> itemGroup = ItemGroups.TOOLS;


    public RadioactiveWaterBucketItem() {
        super(ModFluids.STILL_RADIOACTIVE_WATER, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)
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
