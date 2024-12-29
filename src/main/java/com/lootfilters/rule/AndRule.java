package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Rule;
import lombok.EqualsAndHashCode;
import net.runelite.api.TileItem;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
public class AndRule extends Rule {
    private final List<Rule> rules;

    public AndRule(LootFiltersPlugin plugin, List<Rule> rules) {
        super(plugin, "and");
        this.rules = rules;
    }

    public AndRule(LootFiltersPlugin plugin, Rule... rules) {
        super(plugin, "and");
        this.rules = List.of(rules);
    }

    @Override
    public boolean test(TileItem item) {
        return rules.stream().allMatch(it -> it.test(item));
    }
}
