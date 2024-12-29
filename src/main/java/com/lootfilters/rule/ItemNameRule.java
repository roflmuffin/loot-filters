package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Rule;
import lombok.EqualsAndHashCode;
import net.runelite.api.TileItem;

@EqualsAndHashCode(callSuper = false)
public class ItemNameRule extends Rule {
    private final String name;

    public ItemNameRule(LootFiltersPlugin plugin, String name) {
        super(plugin, "item_name");
        this.name = name;
    }

    @Override
    public boolean test(TileItem item) {
        return plugin.getItemManager().getItemComposition(item.getId()).getName().equals(name);
    }
}
