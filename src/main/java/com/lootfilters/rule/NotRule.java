package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import lombok.EqualsAndHashCode;
import net.runelite.api.TileItem;

@EqualsAndHashCode(callSuper = false)
public class NotRule extends Rule {
    private final Rule inner;

    public NotRule(Rule inner) {
        super("not");

        this.inner = inner;
    }

    @Override
    public boolean test(LootFiltersPlugin plugin, TileItem item) {
        return !inner.test(plugin, item);
    }
}
