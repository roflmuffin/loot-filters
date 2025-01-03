package com.lootfilters;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lootfilters.config.OwnershipFilterMode;
import com.lootfilters.rule.Rule;
import com.lootfilters.serde.ColorDeserializer;
import com.lootfilters.serde.ColorSerializer;
import com.lootfilters.serde.RuleDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.runelite.api.TileItem;

import java.awt.Color;
import java.security.acl.Owner;
import java.util.ArrayList;
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

    public static FilterConfig ownershipFilter(OwnershipFilterMode mode) {
        var rule = new Rule("") {
            @Override
            public boolean test(LootFiltersPlugin plugin, TileItem item) {
                return false;
            }
        };
        var display = DisplayConfig.builder()
                .hidden(true)
                .build();
        return new FilterConfig();
    }
}
