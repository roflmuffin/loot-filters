package com.lootfilters.rule;

import com.lootfilters.Rule;
import net.runelite.api.TileItem;

public class ItemIdRule extends Rule {
    private final int id;

    public ItemIdRule(int id) {
        this.id = id;
    }

    @Override
    public boolean test(TileItem item) {
        return item.getId() == id;
    }
}
