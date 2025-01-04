package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import lombok.EqualsAndHashCode;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;

@EqualsAndHashCode(callSuper = false)
public class ItemValueRule extends ComparatorRule {
    public ItemValueRule(int value, Comparator cmp) {
       super("item_value", value, cmp);
    }

    @Override
    public int getLhs(LootFiltersPlugin plugin, TileItem item) {
        switch (item.getId()) {
            case ItemID.COINS_995:
                return item.getQuantity();
            case ItemID.PLATINUM_TOKEN:
                return item.getQuantity() * 1000;
            default:
                return plugin.getItemManager().getItemPrice(item.getId()) * item.getQuantity();
        }
    }
}
