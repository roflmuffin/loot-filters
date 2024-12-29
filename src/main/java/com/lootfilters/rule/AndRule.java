package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Rule;
import net.runelite.api.TileItem;

import java.util.List;

public class AndRule extends Rule {
    private final List<Rule> rules;

    public AndRule(LootFiltersPlugin plugin, List<Rule> rules) {
        super(plugin);
        this.rules = rules;
    }

    @Override
    public boolean test(TileItem item) {
        return rules.stream().allMatch(it -> it.test(item));
    }
}
