package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Rule;
import net.runelite.api.TileItem;

public class ItemNameRule extends Rule {
    private final String name;

    public ItemNameRule(LootFiltersPlugin plugin, String name) {
        super(plugin);
        this.name = name;
    }

    @Override
    public boolean test(TileItem item) {
        return plugin.getItemManager().getItemComposition(item.getId()).getName().equals(name);
    }
}
