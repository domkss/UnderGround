package dev.domkss.config;

import dev.domkss.jconfiglib.ConfigField;
import net.minecraft.util.math.BlockPos;

public class ModConfig {

    @ConfigField
    private String spawnPos = "0,150,0";

    @ConfigField
    private Integer spawnRadiusY = 5;

    @ConfigField
    private Boolean replaceUndergroundOceansWithRadioactiveWater = true;

    @ConfigField
    private Double radioactiveWaterSpringChance = 0.3;

    @ConfigField
    private Integer maxTickNumberOfRadioactiveWaterExposure = 400;

    @ConfigField
    private Integer maxBonusHealth = 20;

    @ConfigField
    private Integer maxBonusArmor = 20;

    @ConfigField
    private Integer maxBonusSpeed = 20;

    @ConfigField
    private Integer maxBonusHaste = 20;


    public BlockPos getSpawnPos() {
        String[] positions = spawnPos.split(",");
        if (positions.length < 3) return new BlockPos(0, 150, 0);
        return new BlockPos(Integer.parseInt(positions[0]), Integer.parseInt(positions[1]), Integer.parseInt(positions[2]));
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


    public Integer getMaxBonusHealth() {
        return maxBonusHealth;
    }

    public Integer getMaxBonusArmor() {
        return maxBonusArmor;
    }

    public Integer getMaxBonusSpeed() {
        return maxBonusSpeed;
    }

    public Integer getMaxBonusHaste() {
        return maxBonusHaste;
    }
}
