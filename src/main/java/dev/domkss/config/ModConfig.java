package dev.domkss.config;

import net.minecraft.util.math.BlockPos;

public class ModConfig {

    private BlockPos spawnPos = new BlockPos(0,150,0);

    private int spawnRadiusY = 5;

    public BlockPos getSpawnPos() {
        return spawnPos;
    }

    public int getSpawnRadiusY() {
        return spawnRadiusY;
    }
}
