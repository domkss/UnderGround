package dev.domkss.mixin.worldgen;

import dev.domkss.UnderGround;
import dev.domkss.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {


    @Inject(method = "populateEntities", at = @At("TAIL"))
    private void onPopulateEntities(ChunkRegion region, CallbackInfo ci) {

        // If this chunk contains spawn, clear a safe air pocket around spawn underground
        // vertical radius
        clearSpawnArea(region, region.getCenterPos(), UnderGround.config.getSpawnPos());

    }


    @Unique
    private void clearSpawnArea(ChunkRegion region, ChunkPos currentChunkPos, BlockPos spawnPos) {
        int dx = Math.abs(currentChunkPos.x - spawnPos.getX());
        int dz = Math.abs(currentChunkPos.z - spawnPos.getZ());
        if (dx > 1 || dz > 1) return; // Skip chunks outside the 3x3 area

        Chunk chunk = region.getChunk(currentChunkPos.x, currentChunkPos.z);


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
                        if (!region.getFluidState(pos).isEmpty())
                            chunk.setBlockState(pos, Blocks.ANDESITE.getDefaultState(), false);

                    } else {
                        chunk.setBlockState(pos, Blocks.AIR.getDefaultState(), false);
                    }

                }
            }
        }

    }


}