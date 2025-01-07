package com.lootfilters;

import com.google.gson.GsonBuilder;
import com.lootfilters.lang.Lexer;
import com.lootfilters.lang.Parser;
import com.lootfilters.rule.Rule;
import com.lootfilters.serde.ColorDeserializer;
import com.lootfilters.serde.ColorSerializer;
import com.lootfilters.serde.RuleDeserializer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.runelite.api.TileItem;

import java.awt.Color;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class LootFilter {
    private final String name;
    private final List<MatcherConfig> matchers;

    public static LootFilter fromJson(String json) {
        var gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorDeserializer())
                .registerTypeAdapter(Rule.class, new RuleDeserializer())
                .create();
        return gson.fromJson(json, LootFilter.class);
    }

    public static LootFilter fromSource(String source) throws Exception {
        var tokens = new Lexer(source).tokenize();
        return new Parser(tokens).parse();
    }

    public String toJson() {
        var gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorSerializer())
                .create();
        return gson.toJson(this);
    }

    public DisplayConfig findMatch(LootFiltersPlugin plugin, TileItem item) {
        var match = matchers.stream()
                .filter(it -> it.getRule().test(plugin, item))
                .findFirst()
                .orElse(null);
        return match != null ? match.getDisplay() : null;
    }
}
