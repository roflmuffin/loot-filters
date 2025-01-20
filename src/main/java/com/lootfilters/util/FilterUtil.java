package com.lootfilters.util;

import com.lootfilters.LootFilter;
import com.lootfilters.LootFiltersConfig;
import com.lootfilters.MatcherConfig;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.lootfilters.util.TextUtil.quote;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static net.runelite.client.util.ColorUtil.colorToAlphaHexCode;

public class FilterUtil {
    private FilterUtil() {}

    /**
     * Wraps a user-defined loot filter with config defaults (highlight/hide, value tiers, etc.).
     */
    public static LootFilter withConfigMatchers(LootFilter filter, LootFiltersConfig config) {
        var matchersWithConfig = new ArrayList<MatcherConfig>();
        matchersWithConfig.add(MatcherConfig.ownershipFilter(config.ownershipFilter()));

        matchersWithConfig.addAll(filter.getMatchers());

        matchersWithConfig.add(MatcherConfig.highlight(config.highlightedItems(), config.highlightColor(), config.highlightLootbeam()));
        matchersWithConfig.add(MatcherConfig.hide(config.hiddenItems()));

        matchersWithConfig.add(MatcherConfig.valueTier(config.enableInsaneItemValueTier(), config.insaneValue(), config.insaneValueColor(), true));
        matchersWithConfig.add(MatcherConfig.valueTier(config.enableHighItemValueTier(), config.highValue(), config.highValueColor(), true));
        matchersWithConfig.add(MatcherConfig.valueTier(config.enableMediumItemValueTier(), config.mediumValue(), config.mediumValueColor(), false));
        matchersWithConfig.add(MatcherConfig.valueTier(config.enableLowItemValueTier(), config.lowValue(), config.lowValueColor(), false));

        matchersWithConfig.add(MatcherConfig.showUnmatched(config.showUnmatchedItems()));

        if (config.alwaysShowValue()) {
            matchersWithConfig = matchersWithConfig.stream()
                    .map(it -> new MatcherConfig(it.getRule(), it.getDisplay().toBuilder()
                            .showValue(true)
                            .build()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (config.alwaysShowDespawn()) {
            matchersWithConfig = matchersWithConfig.stream()
                    .map(it -> new MatcherConfig(it.getRule(), it.getDisplay().toBuilder()
                            .showDespawn(true)
                            .build()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new LootFilter(filter.getName(), filter.getDescription(), filter.getActivationArea(), matchersWithConfig);
    }

    /**
     * Captures the current config-based item matchers, exporting them to their own filter.
     */
    public static String configToFilterSource(LootFiltersConfig config, String name) {
        var defines = "#define HIGHLIGHT_COLOR " + quote(colorToAlphaHexCode(config.highlightColor()));
        var meta = "meta { name = " + quote(name) + "; }";

        var highlights = "";
        if (!config.highlightedItems().isBlank()) {
            highlights = stream(config.highlightedItems().split(","))
                    .map(it -> "HIGHLIGHT(" + quote(it) + ", HIGHLIGHT_COLOR)")
                    .collect(joining("\n"));
        }
        var hides = "";

        if (!config.hiddenItems().isBlank()) {
            hides = stream(config.hiddenItems().split(","))
                    .map(it -> "HIDE("+ quote(it) + ")")
                    .collect(joining("\n"));
        }

        return String.join("\n",
                defines,
                meta,
                highlights,
                hides,
                toValueTierSource(config.enableInsaneItemValueTier(), config.insaneValue(), config.insaneValueColor(), true),
                toValueTierSource(config.enableHighItemValueTier(), config.highValue(), config.highValueColor(), true),
                toValueTierSource(config.enableMediumItemValueTier(), config.mediumValue(), config.mediumValueColor(), false),
                toValueTierSource(config.enableLowItemValueTier(), config.lowValue(), config.lowValueColor(), false));
    }

    private static String toValueTierSource(boolean enabled, int value, Color color, boolean showLootbeam) {
        if (!enabled) {
            return "";
        }

        return format("if (value:>%d) { color = %s; showLootbeam = %b; }",
                value,
                quote(colorToAlphaHexCode(color)),
                showLootbeam);
    }
}
