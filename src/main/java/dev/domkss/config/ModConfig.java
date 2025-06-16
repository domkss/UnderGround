package dev.domkss.config;

import net.minecraft.util.math.BlockPos;

public class ModConfig {

    private BlockPos spawnPos = new BlockPos(0, 150, 0);

    private Integer spawnRadiusY = 5;


    private Boolean replaceUndergroundOceansWithRadioactiveWater = true;

    private Double radioactiveWaterSpringChance = 0.3;


    private Integer maxTickNumberOfRadioactiveWaterExposure = 500;


    public BlockPos getSpawnPos() {
        return spawnPos;
    }

    public Integer getSpawnRadiusY() {
        return spawnRadiusY;
    }

    public Double getRadioactiveWaterSpringChance() {
        return radioactiveWaterSpringChance;
    }

    public Boolean getReplaceUndergroundOceansWithRadioactiveWater() {
        return replaceUndergroundOceansWithRadioactiveWater;
    }

    public Integer getMaxTickNumberOfRadioactiveWaterExposure() {
        return maxTickNumberOfRadioactiveWaterExposure;
    }


}
