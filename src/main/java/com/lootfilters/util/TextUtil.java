package com.lootfilters.util;

import net.runelite.api.TileItem;

import java.awt.Color;

public class TextUtil {
    private TextUtil() {}

    public static String getBaseDisplayText(TileItem item, String name) {
        var text = name;
        if (item.getQuantity() > 1) {
            text += " (" + item.getQuantity() + ")";
        }
        return text;
    }

    public static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n';
    }

    public static boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isAlpha(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    public static boolean isLegalIdent(char c) {
        return c == '_' || isAlpha(c) || isNumeric(c);
    }

    public static Color parseArgb(String argb) {
        return new Color(Long.decode("0x" + argb).intValue(), true);
    }
}
