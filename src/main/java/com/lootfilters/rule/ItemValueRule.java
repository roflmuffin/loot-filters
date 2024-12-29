package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Comparator;
import lombok.EqualsAndHashCode;
import net.runelite.api.TileItem;

@EqualsAndHashCode(callSuper = false)
public class ItemValueRule extends ComparatorRule {
    public ItemValueRule(int value, Comparator cmp) {
       super("item_value", value, cmp);
    }

    @Override
    public int getLhs(LootFiltersPlugin plugin, TileItem item) {
        return plugin.getItemManager().getItemPrice(item.getId());
    }
}
