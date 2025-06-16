package dev.domkss.config;

import net.minecraft.util.math.BlockPos;

public class ModConfig {

    private BlockPos spawnPos = new BlockPos(0, 150, 0);

    private Integer spawnRadiusY = 5;


    private Double radioactiveWaterChance = 0.5;

    private Double radioactiveWaterSpringChance = 0.5;


    private Integer maxTickNumberOfRadioactiveWaterExposure = 120;


    public BlockPos getSpawnPos() {
        return spawnPos;
    }

    public Integer getSpawnRadiusY() {
        return spawnRadiusY;
    }

    public Double getRadioactiveWaterSpringChance() {
        return radioactiveWaterSpringChance;
    }

    public Double getRadioactiveWaterChance() {
        return radioactiveWaterChance;
    }

    public Integer getMaxTickNumberOfRadioactiveWaterExposure() {
        return maxTickNumberOfRadioactiveWaterExposure;
    }


}
