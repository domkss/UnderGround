package dev.domkss.items;

import dev.domkss.UnderGround;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MatrixPickaxeItem extends PickaxeItem implements CustomItem {

    private static final Identifier identifier = Identifier.of(UnderGround.MOD_ID, "matrix_pickaxe");
    private static final RegistryKey<ItemGroup> itemGroup = ItemGroups.TOOLS;
    private static final ThreadLocal<Boolean> isMiningArea = ThreadLocal.withInitial(() -> false);
    public static final ToolMaterial NETHERITE_SLOW = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 7.0F, 4.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);

    public MatrixPickaxeItem() {
        super(NETHERITE_SLOW, 1, -2.8F, new Settings().maxCount(1)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier)));
    }


    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (world.isClient || !(miner instanceof ServerPlayerEntity player)) {
            return super.postMine(stack, world, state, pos, miner);
        }

        if (isMiningArea.get()) {
            // Prevent recursion
            return super.postMine(stack, world, state, pos, miner);
        }

        try {
            isMiningArea.set(true);
            mine3x3x3(world, pos, player, stack);
        } finally {
            isMiningArea.set(false);
        }

        return super.postMine(stack, world, state, pos, miner);
    }


    private void mine3x3x3(World world, BlockPos origin, ServerPlayerEntity player, ItemStack stack) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos target = origin.add(dx, dy, dz);

                    // Skip the originally broken block
                    if (target.equals(origin)) continue;

                    BlockState targetState = world.getBlockState(target);
                    if (stack.isSuitableFor(targetState) && targetState.getHardness(world, target) >= 0) {
                        player.interactionManager.tryBreakBlock(target);
                    }
                }
            }
        }
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
