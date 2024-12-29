package com.lootfilters;

import net.runelite.api.TileItem;

public abstract class Rule {
    protected final LootFiltersPlugin plugin;

    protected Rule(LootFiltersPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract boolean test(TileItem item);
}
