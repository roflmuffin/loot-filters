package com.lootfilters;

import net.runelite.api.TileItem;

public abstract class Rule {
    public abstract boolean test(TileItem item);
}
