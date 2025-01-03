package com.lootfilters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.awt.Color;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class DisplayConfig {
    @Builder.Default
    private final Color textColor = Color.WHITE;

    private final Color backgroundColor;
    private final Color borderColor;
    private final boolean hidden;
    private final boolean showLootbeam;
    private final boolean showValue;
    private final boolean showDespawn;

    public DisplayConfig(Color textColor) {
        this.textColor = textColor;
        backgroundColor = null;
        borderColor = null;
        hidden = false;
        showLootbeam = false;
        showValue = false;
        showDespawn = false;
    }
}
