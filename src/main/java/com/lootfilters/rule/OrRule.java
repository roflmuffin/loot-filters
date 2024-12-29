package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.Rule;
import lombok.EqualsAndHashCode;
import net.runelite.api.TileItem;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
public class OrRule extends Rule {
    private final List<Rule> rules;

    public OrRule(List<Rule> rules) {
        super("or");
        this.rules = rules;
    }

    @Override
    public boolean test(LootFiltersPlugin plugin, TileItem item) {
        return rules.stream().anyMatch(it -> it.test(plugin, item));
    }
}
