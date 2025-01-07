package com.lootfilters;

import com.lootfilters.rule.AndRule;
import com.lootfilters.rule.Comparator;
import com.lootfilters.rule.ItemIdRule;
import com.lootfilters.rule.ItemNameRule;
import com.lootfilters.rule.ItemQuantityRule;
import com.lootfilters.rule.ItemValueRule;
import com.lootfilters.rule.OrRule;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

public class LootFilterSerdeTest {
    public static void main(String[] args) throws Exception {
        var filter = new LootFilter(
                "foo",
                "bar",
                List.of(
                        new MatcherConfig(new ItemIdRule(1), new DisplayConfig(Color.RED)),
                        new MatcherConfig(new ItemNameRule("bandos-crossbow"), new DisplayConfig(Color.GREEN)),
                        new MatcherConfig(new ItemValueRule(1_000, Comparator.LT), new DisplayConfig(Color.BLUE)),
                        new MatcherConfig(new ItemQuantityRule(1_000, Comparator.LT), new DisplayConfig(Color.WHITE)),
                        new MatcherConfig(
                                new AndRule(List.of(
                                        new ItemNameRule("Coins"),
                                        new ItemQuantityRule(5, Comparator.EQ)
                                )),
                                new DisplayConfig(Color.WHITE.darker())
                        ),
                        new MatcherConfig(
                                new OrRule(List.of(
                                        new ItemNameRule("Coins"),
                                        new ItemQuantityRule(9, Comparator.EQ)
                                )),
                                new DisplayConfig(Color.WHITE.darker())
                        )
                )
        );

        var ser = filter.toJson();
        var deser = LootFilter.fromJson(ser);

        if (!Objects.deepEquals(filter, deser)) {
            throw new Exception("serialized and deserialized rules do not match");
        }
    }
}
