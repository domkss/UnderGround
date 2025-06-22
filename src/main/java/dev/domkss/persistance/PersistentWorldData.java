package dev.domkss.persistance;

import com.mojang.datafixers.util.Pair;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.nbt.NbtElement;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class PersistentWorldData extends PersistentState {
    private static final String DATA_NAME = "underground_data";

    public static final Type<PersistentWorldData> TYPE = new Type<>(
            PersistentWorldData::new,
            PersistentWorldData::fromNbt,
            DataFixTypes.SAVED_DATA_MAP_INDEX
    );

    private final Map<String, Object> data = new HashMap<>();

    public PersistentWorldData() {}


    // Utility to fetch this data object
    public static PersistentWorldData get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TYPE, DATA_NAME);
    }

    // Save a key-value pair
    public void saveData(Pair<String, Object> pair) {
        data.put(pair.getFirst(), pair.getSecond());
        markDirty();
    }

    // Retrieve a value by key
    public Object getDataByKey(String key) {
        return data.get(key);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        for (Map.Entry<String, Object> entry : this.data.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String s) nbt.putString(entry.getKey(), s);
            else if (value instanceof Integer i) nbt.putInt(entry.getKey(), i);
            else if (value instanceof Boolean b) nbt.putBoolean(entry.getKey(), b);
            else if (value instanceof Double d) nbt.putDouble(entry.getKey(), d);
        }
        return nbt;
    }

    private static PersistentWorldData fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        PersistentWorldData data = new PersistentWorldData();
        for (String key : nbt.getKeys()) {
            NbtElement element = nbt.get(key);
            switch (Objects.requireNonNull(element).getType()) {
                case NbtElement.STRING_TYPE -> data.data.put(key, nbt.getString(key));
                case NbtElement.INT_TYPE -> data.data.put(key, nbt.getInt(key));
                case NbtElement.BYTE_TYPE -> data.data.put(key, nbt.getBoolean(key));
                case NbtElement.DOUBLE_TYPE -> data.data.put(key, nbt.getDouble(key));
            }
        }
        return data;
    }



}