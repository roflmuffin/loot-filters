package com.lootfilters;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lootfilters.rule.Rule;
import com.lootfilters.serde.ColorDeserializer;
import com.lootfilters.serde.ColorSerializer;
import com.lootfilters.serde.RuleDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.runelite.api.TileItem;
import net.runelite.api.Varbits;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@EqualsAndHashCode
public class FilterConfig {
    private final Rule rule;
    private final DisplayConfig display;

    public static String toJson(List<FilterConfig> filters) {
        var gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorSerializer())
                .create();
        return gson.toJson(filters);
    }

    public static List<FilterConfig> fromJson(String json) {
        var gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorDeserializer())
                .registerTypeAdapter(Rule.class, new RuleDeserializer())
                .create();
        return gson.fromJson(json, new TypeToken<ArrayList<FilterConfig>>() {}.getType());
    }

    public static DisplayConfig findMatch(List<FilterConfig> filters, LootFiltersPlugin plugin, TileItem item) {
        var match = filters.stream()
                .filter(it -> it.rule.test(plugin, item))
                .findFirst()
                .orElse(null);
        return match != null ? match.display : null;
    }

    public FilterConfig(Rule rule, DisplayConfig display) {
        this.rule = rule;
        this.display = display;
    }

    public boolean test(LootFiltersPlugin plugin, TileItem item) {
        return rule.test(plugin, item);
    }

    public static FilterConfig ownershipFilter(boolean enabled) {
        var rule = new Rule("") {
            @Override
            public boolean test(LootFiltersPlugin plugin, TileItem item) {
                var accountType = plugin.getClient().getVarbitValue(Varbits.ACCOUNT_TYPE);
                return enabled && accountType != 0 && item.getOwnership() == TileItem.OWNERSHIP_OTHER;
            }
        };
        var display = DisplayConfig.builder()
                .hidden(true)
                .build();
        return new FilterConfig(rule, display);
    }

    public static FilterConfig showUnmatched(boolean enabled) {
        var rule = new Rule("") {
            @Override
            public boolean test(LootFiltersPlugin plugin, TileItem item) {
                return enabled;
            }
        };
        var display = DisplayConfig.builder()
                .textColor(Color.WHITE)
                .build();
        return new FilterConfig(rule, display);
    }

    public static FilterConfig valueTier(boolean enabled, int value, Color color, boolean showLootbeam) {
        var rule = new Rule("") {
            @Override
            public boolean test(LootFiltersPlugin plugin, TileItem item) {
                var price = plugin.getItemManager().getItemPrice(item.getId());
                return enabled && price >= value;
            }
        };
        var display = DisplayConfig.builder()
                .textColor(color)
                .showLootbeam(showLootbeam)
                .showValue(true)
                .build();
        return new FilterConfig(rule, display);
    }

    public static FilterConfig highlight(String rawNames, Color color) {
        var names = rawNames.split(",");
        var rule = new Rule("") {
            @Override
            public boolean test(LootFiltersPlugin plugin, TileItem item) {
                var name = plugin.getItemManager().getItemComposition(item.getId()).getName();
                return Arrays.stream(names).anyMatch(it -> it.equalsIgnoreCase(name));
            }
        };
        var display = DisplayConfig.builder()
                .textColor(color)
                .build();
        return new FilterConfig(rule, display);
    }

    public static FilterConfig hide(String rawNames) {
        var names = rawNames.split(",");
        var rule = new Rule("") {
            @Override
            public boolean test(LootFiltersPlugin plugin, TileItem item) {
                var name = plugin.getItemManager().getItemComposition(item.getId()).getName();
                return Arrays.stream(names).anyMatch(it -> it.equalsIgnoreCase(name));
            }
        };
        var display = DisplayConfig.builder()
                .hidden(true)
                .build();
        return new FilterConfig(rule, display);
    }
}
