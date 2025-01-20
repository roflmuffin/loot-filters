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
        if (name.startsWith("*")) {
            return itemName.toLowerCase().endsWith(name.toLowerCase().substring(1));
        } else if (name.endsWith("*")) {
            return itemName.toLowerCase().startsWith(name.toLowerCase().substring(0, name.length() - 1));
        }
        return itemName.equalsIgnoreCase(name);
    }
}
