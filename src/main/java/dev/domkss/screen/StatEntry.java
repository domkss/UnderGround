package dev.domkss.screen;

import net.minecraft.util.Identifier;

public class StatEntry {
    public String name;
    public int value;
    public int maxValue;
    public Identifier icon;

    public StatEntry(String name, int value,int maxValue ,Identifier icon) {
        this.name = name;
        this.value = value;
        this.maxValue=maxValue;
        this.icon = icon;
    }

    public StatEntry(String name, int maxValue, Identifier icon) {
        this(name, 0, maxValue, icon);
    }

}
