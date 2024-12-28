package com.lootfilters.rule;

import com.lootfilters.Rule;
import net.runelite.api.TileItem;

public class ItemNameRule extends Rule {
    @Override
    public boolean test(TileItem item) {
        return false;
    }
}
