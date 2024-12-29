package com.lootfilters;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.awt.Color;

@Getter
@EqualsAndHashCode
public class DisplayConfig {
    private final Color color;
    private final boolean hidden;
    private final boolean showLootbeam;

    public DisplayConfig(Color color, boolean hidden, boolean showLootBeam) {
        this.color = color;
        this.hidden = hidden;
        this.showLootbeam = showLootBeam;
    }
}
