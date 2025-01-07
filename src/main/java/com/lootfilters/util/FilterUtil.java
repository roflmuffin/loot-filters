package com.lootfilters.util;

import com.lootfilters.LootFilter;
import com.lootfilters.LootFiltersConfig;
import com.lootfilters.MatcherConfig;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FilterUtil {
    private FilterUtil() {}

    // wraps a user-defined loot filter with config defaults (highlight/hide, value tiers, etc.)
    public static LootFilter withConfigMatchers(LootFilter filter, LootFiltersConfig config) {
        var matchersWithConfig = new ArrayList<MatcherConfig>();
        matchersWithConfig.add(MatcherConfig.ownershipFilter(config.ownershipFilter()));

        matchersWithConfig.addAll(filter.getMatchers());

        matchersWithConfig.add(MatcherConfig.highlight(config.highlightedItems(), config.highlightColor()));
        matchersWithConfig.add(MatcherConfig.hide(config.hiddenItems()));

        matchersWithConfig.add(MatcherConfig.valueTier(config.enableInsaneItemValueTier(), config.insaneValue(), config.insaneValueColor(), true));
        matchersWithConfig.add(MatcherConfig.valueTier(config.enableHighItemValueTier(), config.highValue(), config.highValueColor(), true));
        matchersWithConfig.add(MatcherConfig.valueTier(config.enableMediumItemValueTier(), config.mediumValue(), config.mediumValueColor(), false));
        matchersWithConfig.add(MatcherConfig.valueTier(config.enableLowItemValueTier(), config.lowValue(), config.lowValueColor(), false));

        matchersWithConfig.add(MatcherConfig.showUnmatched(config.showUnmatchedItems()));

        if (config.alwaysShowValue()) {
            matchersWithConfig = filter.getMatchers().stream()
                    .map(it -> new MatcherConfig(it.getRule(), it.getDisplay().toBuilder()
                            .showValue(true)
                            .build()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (config.alwaysShowDespawn()) {
            matchersWithConfig = filter.getMatchers().stream()
                    .map(it -> new MatcherConfig(it.getRule(), it.getDisplay().toBuilder()
                            .showDespawn(true)
                            .build()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new LootFilter(filter.getName(), filter.getDescription(), filter.getActivationArea(), matchersWithConfig);
    }
}
