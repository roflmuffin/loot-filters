package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Rule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.runelite.api.TileItem;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ItemIdRule extends Rule {
    private final int id;

    public ItemIdRule(LootFiltersPlugin plugin, int id) {
        super(plugin, "item_id");
        this.id = id;
    }

    @Override
    public boolean test(TileItem item) {
        return item.getId() == id;
    }
}
