package dev.domkss.persistance;

import java.util.*;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerStatManager {

    private final PersistentWorldData persistentWorldData;
    private final UUID playerUUID;
    private final Map<StatType, Integer> stats = new EnumMap<>(StatType.class);

    public PlayerStatManager(ServerPlayerEntity serverPlayerEntity) {
        persistentWorldData = PersistentWorldData.get(Objects.requireNonNull(serverPlayerEntity.getServer()).getOverworld());
        this.playerUUID = serverPlayerEntity.getUuid();
        loadStats();
    }

    private void loadStats() {
        for (StatType stat : StatType.values()) {
            String key = generateKey(stat);
            int value = (int) Optional.ofNullable(persistentWorldData.getDataByKey(key)).orElse(0);
            stats.put(stat, value);
        }
    }


    public int getStat(StatType stat) {
        return stats.getOrDefault(stat, 0);
    }

    public void setStat(StatType stat, int value) {
        stats.put(stat, value);
        persistentWorldData.saveData(new Pair<>(generateKey(stat), value));
    }

    public int getStatByKey(String key) {
        StatType statType = StatType.fromKey(key);
        return statType != null ? getStat(statType) : 0;
    }

    public void setStatByKey(String key, int value) {
        StatType statType = StatType.fromKey(key);
        if (statType != null) {
            setStat(statType, value);
        }
    }

    private String generateKey(StatType stat) {
        return playerUUID.toString() + "_player_stats_bonus_" + stat.getKey();
    }

    public enum StatType {
        HEALTH("health"),
        ARMOR("armor"),
        SPEED("speed"),
        HASTE("haste"),
        RADIATION_RESISTANCE("resistance");

        private final String key;

        StatType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public static StatType fromKey(String key) {
            for (StatType stat : values()) {
                if (stat.key.equalsIgnoreCase(key)) {
                    return stat;
                }
            }
            return null;
        }
    }
}

