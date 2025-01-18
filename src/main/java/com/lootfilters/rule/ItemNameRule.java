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
        var itemName = plugin.getItemName(item.getId());
        if (itemName.startsWith("*")) {
            return itemName.toLowerCase().endsWith(name.toLowerCase());
        } else if (itemName.endsWith("*")) {
            return itemName.toLowerCase().startsWith(name.toLowerCase());
        }
        return itemName.equalsIgnoreCase(name);
    }
}
