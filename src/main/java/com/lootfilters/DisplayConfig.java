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
    private final boolean showQuantity;
    private final boolean showValue;
    private final boolean showDespawn;

    public DisplayConfig(Color color) {
        this.color = color;
        hidden = false;
        showLootbeam = false;
        showQuantity = false;
        showValue = false;
        showDespawn = false;
    }

    public DisplayConfig(Color color,
                         boolean hidden,
                         boolean showLootBeam,
                         boolean showQuantity,
                         boolean showValue,
                         boolean showDespawn) {
        this.color = color;
        this.hidden = hidden;
        this.showLootbeam = showLootBeam;
        this.showQuantity = showQuantity;
        this.showValue = showValue;
        this.showDespawn = showDespawn;
    }
}
