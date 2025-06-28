package dev.domkss.mixin.worldgen;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
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


    }


}
