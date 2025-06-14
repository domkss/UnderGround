package dev.domkss.mixin.worldgen;

import dev.domkss.UnderGround;
import dev.domkss.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {


    @Shadow
    public abstract RegistryEntry<ChunkGeneratorSettings> getSettings();

    @Inject(
            method = "buildSurface(Lnet/minecraft/world/ChunkRegion;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/chunk/Chunk;)V",
            at = @At("TAIL")
    )
    public void makeUndergroundWorld(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk, CallbackInfo ci) {


        //Skip if this is not Overworld
        if (!region.toServerWorld().getRegistryKey().equals(World.OVERWORLD)) {
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
                        if (chunk.getBlockState(pos).getBlock() == Blocks.AIR) {
                            chunk.setBlockState(pos, Blocks.STONE.getDefaultState(), false);
                        }
                    }
                }
            }
        }


    }


    @Inject(method = "populateEntities", at = @At("TAIL"))
    private void onPopulateEntities(ChunkRegion region, CallbackInfo ci) {
        // If this chunk contains spawn, clear a safe air pocket around spawn underground
        clearSpawnArea(region, region.getCenterPos(), UnderGround.config.getSpawnPos());
    }


    @Unique
    private void clearSpawnArea(ChunkRegion region, ChunkPos currentChunkPos, BlockPos spawnPos) {
        Chunk chunk = region.getChunk(currentChunkPos.x, currentChunkPos.z);

        int dx = Math.abs(currentChunkPos.x - spawnPos.getX());
        int dz = Math.abs(currentChunkPos.z - spawnPos.getZ());


        if (dx > 1 || dz > 1) return; // Skip chunks outside the 3x3 area

        int radiusY = 5; // vertical radius
        int chunkStartX = currentChunkPos.getStartX();
        int chunkStartZ = currentChunkPos.getStartZ();

        int chunkEndX = chunkStartX + 15;
        int chunkEndZ = chunkStartZ + 15;
        BlockPos.Mutable pos = new BlockPos.Mutable();


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