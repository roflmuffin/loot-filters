package com.lootfilters;

import com.lootfilters.rule.FontType;
import com.lootfilters.rule.TextAccent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.runelite.client.ui.FontManager;

import java.awt.Color;
import java.awt.Font;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode
public class DisplayConfig {
    private static final Color DEFAULT_MENU_TEXT_COLOR = Color.decode("#ff9040");

    private final Color textColor;
    private final Color backgroundColor;
    private final Color borderColor;
    private final Boolean hidden;
    private final Boolean showLootbeam;
    private final Boolean showValue;
    private final Boolean showDespawn;
    private final Boolean notify;
    private final TextAccent textAccent;
    private final Color textAccentColor;
    private final Color lootbeamColor;
    private final FontType fontType;
    private final Color menuTextColor;

    public DisplayConfig(Color textColor) {
        this.textColor = textColor;
        backgroundColor = null;
        borderColor = null;
        hidden = false;
        showLootbeam = false;
        showValue = false;
        showDespawn = false;
        notify = false;
        textAccent = null;
        textAccentColor = null;
        lootbeamColor = null;
        fontType = null;
        menuTextColor = null;
    }

    public Color getLootbeamColor() {
        return lootbeamColor != null ? lootbeamColor : textColor;
    }

    public Color getTextColor() {
        return textColor != null ? textColor : Color.WHITE;
    }

    public Color getMenuTextColor() {
        if (isHidden()) {
            return DEFAULT_MENU_TEXT_COLOR;
        }
        if (menuTextColor != null) {
            return menuTextColor;
        }
        return textColor != null ? textColor : DEFAULT_MENU_TEXT_COLOR;
    }

    public Font getFont() {
        if (fontType == null || fontType == FontType.NORMAL) {
            return FontManager.getRunescapeSmallFont();
        }
        return FontManager.getRunescapeFont();
    }

    public boolean isHidden() { return hidden != null && hidden; }
    public boolean isShowLootbeam() { return !isHidden() && showLootbeam != null && showLootbeam; }
    public boolean isShowValue() { return showValue != null && showValue; }
    public boolean isShowDespawn() { return showDespawn != null && showDespawn; }
    public boolean isNotify() { return !isHidden() && notify != null && notify; }

    public DisplayConfig merge(DisplayConfig other) {
        var b = toBuilder();
        if (other.textColor != null) { b.textColor(other.textColor); }
        if (other.backgroundColor != null) { b.backgroundColor(other.backgroundColor); }
        if (other.borderColor != null) { b.borderColor(other.borderColor); }
        if (other.hidden != null) { b.hidden(other.hidden); }
        if (other.showLootbeam != null) { b.showLootbeam(other.showLootbeam); }
        if (other.showValue != null) { b.showValue(other.showValue); }
        if (other.showDespawn != null) { b.showDespawn(other.showDespawn); }
        if (other.notify != null) { b.notify(other.notify); }
        if (other.textAccent != null) { b.textAccent(other.textAccent); }
        if (other.textAccentColor != null) { b.textAccentColor(other.textAccentColor); }
        if (other.lootbeamColor != null) { b.lootbeamColor(other.lootbeamColor); }
        if (other.fontType != null) { b.fontType(other.fontType); }
        if (other.menuTextColor != null) { b.menuTextColor(other.menuTextColor); }
        return b.build();
    }
}
