package com.lootfilters.util;

import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;

public class RuneliteUtil {
    private RuneliteUtil() {}

    public static boolean isGroundItem(MenuEntry entry) {
        var type = entry.getType();
        return type == MenuAction.GROUND_ITEM_FIRST_OPTION
                || type == MenuAction.GROUND_ITEM_SECOND_OPTION
                || type == MenuAction.GROUND_ITEM_THIRD_OPTION
                || type == MenuAction.GROUND_ITEM_FOURTH_OPTION
                || type == MenuAction.GROUND_ITEM_FIFTH_OPTION
                || type == MenuAction.EXAMINE_ITEM_GROUND;
    }
}
