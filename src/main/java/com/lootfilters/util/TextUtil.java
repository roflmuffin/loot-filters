package com.lootfilters.util;

import net.runelite.api.TileItem;

public class TextUtil {
    private TextUtil() {}

    public static String getBaseDisplayText(TileItem item, String name) {
        var text = name;
        if (item.getQuantity() > 1) {
            text += " (" + item.getQuantity() + ")";
        }
        return text;
    }
}
