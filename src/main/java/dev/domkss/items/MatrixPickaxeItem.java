package dev.domkss.items;

import dev.domkss.UnderGround;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MatrixPickaxeItem extends MiningToolItem implements CustomItem {

    private static final Identifier identifier = Identifier.of(UnderGround.MOD_ID, "matrix_pickaxe");
    private static final RegistryKey<ItemGroup> itemGroup = ItemGroups.TOOLS;
    private static final ThreadLocal<Boolean> isMiningArea = ThreadLocal.withInitial(() -> false);
    public static final ToolMaterial MATRIX_PICKAXE_MATERIAL = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 4062, 8.0F, 4.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);
    public static RegistryEntryList<Block> EFFECTIVE_BLOCKS = null;

    public MatrixPickaxeItem() {
        super(MATRIX_PICKAXE_MATERIAL, BlockTags.PICKAXE_MINEABLE,
                1, -2.8F, new Settings().maxCount(1)
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

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        getCombinedList(world);
    }

    @Override
    public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
        return EFFECTIVE_BLOCKS != null && state.isIn(EFFECTIVE_BLOCKS);
    }


    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        return EFFECTIVE_BLOCKS != null && state.isIn(EFFECTIVE_BLOCKS) ? MATRIX_PICKAXE_MATERIAL.speed() : 1.0f;
    }


    private void getCombinedList(World world) {

        if (EFFECTIVE_BLOCKS == null) {

            Registry<Block> blockLookup = world.getRegistryManager().getOrThrow(Registries.BLOCK.getKey());

            List<RegistryEntry<Block>> entries = new ArrayList<>();
            Consumer<TagKey<Block>> addTagEntries = tag ->
                    blockLookup.getOptional(tag).ifPresent(list -> list.forEach(entries::add));
            addTagEntries.accept(BlockTags.PICKAXE_MINEABLE);
            addTagEntries.accept(BlockTags.AXE_MINEABLE);
            addTagEntries.accept(BlockTags.SHOVEL_MINEABLE);
            EFFECTIVE_BLOCKS = RegistryEntryList.of(entries);
        }

    }


}
