package com.lootfilters;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.awt.Color;

@Getter
@EqualsAndHashCode
public class DisplayConfig {
    private final Color textColor;
    private final Color backgroundColor;
    private final Color borderColor;
    private final boolean hidden;
    private final boolean showLootbeam;
    private final boolean showQuantity;
    private final boolean showValue;
    private final boolean showDespawn;

    public DisplayConfig(Color textColor) {
        this.textColor = textColor;
        backgroundColor = null;
        borderColor = null;
        hidden = false;
        showLootbeam = false;
        showQuantity = false;
        showValue = false;
        showDespawn = false;
    }

    public DisplayConfig(Color textColor, Color backgroundColor, Color borderColor,
                         boolean hidden,
                         boolean showLootBeam,
                         boolean showQuantity,
                         boolean showValue,
                         boolean showDespawn) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.hidden = hidden;
        this.showLootbeam = showLootBeam;
        this.showQuantity = showQuantity;
        this.showValue = showValue;
        this.showDespawn = showDespawn;
    }
}
