package com.lootfilters;

import net.runelite.api.TileItem;

public abstract class Rule {
    protected transient final LootFiltersPlugin plugin;
    protected final String discriminator; // serde discriminator

    protected Rule(LootFiltersPlugin plugin, String discriminator) {
        this.plugin = plugin;
        this.discriminator = discriminator;
    }

    public abstract boolean test(TileItem item);
}
