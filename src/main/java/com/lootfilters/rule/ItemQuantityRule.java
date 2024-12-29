package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Comparator;
import lombok.EqualsAndHashCode;
import net.runelite.api.TileItem;

@EqualsAndHashCode(callSuper = false)
public class ItemQuantityRule extends ComparatorRule {
    public ItemQuantityRule(LootFiltersPlugin plugin, int value, Comparator cmp) {
        super(plugin, "item_quantity", value, cmp);
    }

    @Override
    public int getLhs(TileItem item) {
        return item.getQuantity();
    }
}
