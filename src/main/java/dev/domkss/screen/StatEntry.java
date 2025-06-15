package dev.domkss.screen;

import net.minecraft.util.Identifier;

public class StatEntry {
    public String name;
    public int value;
    public Identifier icon;

    public StatEntry(String name, Identifier icon) {
        this.name = name;
        this.icon = icon;
        this.value = 0;
    }
}
