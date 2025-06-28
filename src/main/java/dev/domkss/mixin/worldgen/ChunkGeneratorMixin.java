package dev.domkss.mixin.worldgen;

import dev.domkss.UnderGround;
import dev.domkss.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {


    @Inject(
            method = "generateFeatures",
            at = @At("TAIL")
    )
    public void makeUndergroundWorld(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor, CallbackInfo ci) {


        //Skip if this is not Overworld
        if (!world.toServerWorld().getRegistryKey().equals(World.OVERWORLD)) {
            return;
        }
        //Skip flat world type
        if (world.toServerWorld().isFlat()) {
            return;
        }


        int bottomY = chunk.getBottomY();
        int topY = chunk.getTopYInclusive();

        int chunkStartX = chunk.getPos().getStartX();
        int chunkStartZ = chunk.getPos().getStartZ();

        for (int y = bottomY; y <= topY; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos pos = new BlockPos(chunkStartX + x, y, chunkStartZ + z);
                    if (y == bottomY || y == topY) {
                        chunk.setBlockState(pos, Blocks.BEDROCK.getDefaultState(), false);
                    } else {
                        if (y > 55 && chunk.getBlockState(pos).getBlock() == Blocks.AIR) {
                            chunk.setBlockState(pos, Blocks.STONE.getDefaultState(), false);
                        }
                    }
                }
            }
        }

        // If this chunk contains spawn, clear a safe air pocket around spawn underground
        // vertical radius
        clearSpawnArea(chunk, UnderGround.config.getSpawnPos());
    }

    @Unique
    private void clearSpawnArea(Chunk chunk, BlockPos spawnPos) {
        ChunkPos currentChunkPos = chunk.getPos();
        int dx = Math.abs(currentChunkPos.x - spawnPos.getX());
        int dz = Math.abs(currentChunkPos.z - spawnPos.getZ());
        if (dx > 1 || dz > 1) return; // Skip chunks outside the 3x3 area


        int chunkStartX = currentChunkPos.getStartX();
        int chunkStartZ = currentChunkPos.getStartZ();

        int chunkEndX = chunkStartX + 15;
        int chunkEndZ = chunkStartZ + 15;
        BlockPos.Mutable pos = new BlockPos.Mutable();
        int radiusY = UnderGround.config.getSpawnRadiusY();


        // Clear the entire chunk in Y +/- radius
        Block lightBlock = ModBlocks.GLOWING_UNBREAKABLE_STONE;
        for (int x = chunkStartX; x <= chunkEndX; x++) {
            for (int y = spawnPos.getY() - radiusY; y <= spawnPos.getY() + radiusY; y++) {
                for (int z = chunkStartZ; z <= chunkEndZ; z++) {
                    pos.set(x, y, z);

                    //Bottom Layer of spawn, make a platform
                    if (y == spawnPos.getY() - radiusY) {
                        chunk.setBlockState(pos, lightBlock.getDefaultState(), false);
                    }
                    //Top Layer of spawn, make a platform
                    else if (y == spawnPos.getY() + radiusY) {
                        chunk.setBlockState(pos, lightBlock.getDefaultState(), false);
                    }
                    //Side walls to prevent lava/water inflow
                    else if ((currentChunkPos.x - spawnPos.getX() == -1 && x == chunkStartX) ||
                            (currentChunkPos.x - spawnPos.getX() == 1 && x == chunkEndX) ||
                            (currentChunkPos.z - spawnPos.getZ() == -1 && z == chunkStartZ) ||
                            (currentChunkPos.z - spawnPos.getZ() == 1 && z == chunkEndZ)
                    ) {
                        if (!chunk.getFluidState(pos).isEmpty())
                            chunk.setBlockState(pos, Blocks.ANDESITE.getDefaultState(), false);

                    } else {
                        chunk.setBlockState(pos, Blocks.AIR.getDefaultState(), false);
                    }

                }
            }
        }

    }


}
