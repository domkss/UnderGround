package dev.domkss.mixin;

import dev.domkss.UnderGround;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(NoiseChunkGenerator.class)
public abstract class ChunkGeneratorMixin {



	@Inject(
			method = "buildSurface(Lnet/minecraft/world/ChunkRegion;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/chunk/Chunk;)V",
			at = @At("HEAD")
	)
	public void makeUndergroundWorld(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk, CallbackInfo ci) {

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
						chunk.setBlockState(pos, Blocks.STONE.getDefaultState(), false);
					}
				}
			}
		}

		// If this chunk contains spawn, clear a safe air pocket around spawn underground
		clearSpawnArea(chunk, UnderGround.config.getSpawnPos());



	}

	@Unique
	private void clearSpawnArea(Chunk chunk, BlockPos spawnPos) {
		// Only clear if this chunk is in the 3x3 area around the spawn chunk
		ChunkPos currentChunkPos = chunk.getPos();

		int dx = Math.abs(currentChunkPos.x - spawnPos.getX());
		int dz = Math.abs(currentChunkPos.z - spawnPos.getZ());

		if (dx > 1 || dz > 1) return; // Skip chunks outside the 3x3 area


		// Clear the entire chunk in Y +/- radius, not just a small cube
		int radiusY = 4; // vertical radius
		int chunkStartX = chunk.getPos().getStartX();
		int chunkStartZ = chunk.getPos().getStartZ();


		int chunkEndX = chunkStartX + 15;
		int chunkEndZ = chunkStartZ + 15;

		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int x = chunkStartX; x <= chunkEndX; x++) {
			for (int y = spawnPos.getY() - radiusY; y <= spawnPos.getY() + radiusY; y++) {
				for (int z = chunkStartZ; z <= chunkEndZ; z++) {
					pos.set(x, y, z);
					chunk.setBlockState(pos, Blocks.AIR.getDefaultState(), false);
				}
			}
		}
	}
}