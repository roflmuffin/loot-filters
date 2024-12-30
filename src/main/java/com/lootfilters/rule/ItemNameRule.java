package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import lombok.EqualsAndHashCode;
import net.runelite.api.TileItem;

@EqualsAndHashCode(callSuper = false)
public class ItemNameRule extends Rule {
    private final String name;

    public ItemNameRule(String name) {
        super("item_name");
        this.name = name;
    }

    @Override
    public boolean test(LootFiltersPlugin plugin, TileItem item) {
        return plugin.getItemManager().getItemComposition(item.getId()).getName().equals(name);
    }
}
