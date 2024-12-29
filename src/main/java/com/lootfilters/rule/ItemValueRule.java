package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Operator;
import com.lootfilters.Rule;
import net.runelite.api.TileItem;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;

public class ItemValueRule extends Rule {
    @Inject
    private ItemManager itemManager;

    private final int value;
    private final Operator op;

    public ItemValueRule(LootFiltersPlugin plugin, int value, Operator op) {
       super(plugin);
       this.value = value;
       this.op = op;
    }

    @Override
    public boolean test(TileItem item) {
        return false;
    }
}
