package com.lootfilters;

import net.runelite.api.TileItem;

public abstract class Rule {
    protected final String discriminator; // serde discriminator

    protected Rule(String discriminator) {
        this.discriminator = discriminator;
    }

    public abstract boolean test(LootFiltersPlugin plugin, TileItem item);
}
