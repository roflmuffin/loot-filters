package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Rule;
import net.runelite.api.TileItem;

public class ItemIdRule extends Rule {
    private final int id;

    public ItemIdRule(LootFiltersPlugin plugin, int id) {
        super(plugin);
        this.id = id;
    }

    @Override
    public boolean test(TileItem item) {
        return item.getId() == id;
    }
}
