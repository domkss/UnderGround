package dev.domkss.config;

import dev.domkss.jconfiglib.ConfigField;
import net.minecraft.util.math.BlockPos;

public class ModConfig {

    @ConfigField
    private String spawnPos = "0,80,0";

    @ConfigField
    private Integer spawnRadiusY = 5;

    @ConfigField
    private Integer worldTopY = 100;

    @ConfigField
    private Boolean replaceUndergroundOceansWithRadioactiveWater = true;

    @ConfigField
    private Double radioactiveWaterSpringChance = 0.3;

    @ConfigField
    private Integer maxTickNumberOfRadioactiveWaterExposure = 400;

    @ConfigField
    private Integer maxBonusHealth = 10;

    @ConfigField
    private Integer maxBonusArmor = 5;

    @ConfigField
    private Integer maxBonusSpeed = 10;

    @ConfigField
    private Integer maxBonusHaste = 15;

    @ConfigField
    private Integer maxBonusRadiationResistance = 15;

    @ConfigField
    private Float statUpgradeSpawnChance = 0.02f;

    @ConfigField
    private Float miningCoreUpgradeSpawnChance = 0.04f;

    public BlockPos getSpawnPos() {
        String[] positions = spawnPos.split(",");
        if (positions.length < 3) return new BlockPos(0, 60, 0);
        return new BlockPos(Integer.parseInt(positions[0]), Integer.parseInt(positions[1]), Integer.parseInt(positions[2]));
    }



    public Integer getSpawnRadiusY() {
        return spawnRadiusY;
    }

    public Double getRadioactiveWaterSpringChance() {
        return radioactiveWaterSpringChance;
    }

    public Integer getWorldTopY() {
        return worldTopY;
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

    public Integer getMaxBonusRadiationResistance() {
        return maxBonusRadiationResistance;
    }

    public Float getStatUpgradeSpawnChance() {
        return statUpgradeSpawnChance;
    }

    public Float getMiningCoreUpgradeSpawnChance() {
        return miningCoreUpgradeSpawnChance;
    }
}
