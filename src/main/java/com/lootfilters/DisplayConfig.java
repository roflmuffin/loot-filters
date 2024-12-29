package com.lootfilters;

import lombok.Getter;

import java.awt.Color;

public class DisplayConfig {
    @Getter private final Color color;
    private final boolean hidden;

    public DisplayConfig(Color color, boolean hidden) {
        this.color = color;
        this.hidden = hidden;
    }
}
