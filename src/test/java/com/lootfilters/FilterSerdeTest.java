package com.lootfilters;

import com.lootfilters.rule.AndRule;
import com.lootfilters.rule.ItemIdRule;
import com.lootfilters.rule.ItemNameRule;
import com.lootfilters.rule.ItemQuantityRule;
import com.lootfilters.rule.ItemValueRule;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

public class FilterSerdeTest {
    public static void main(String[] args) throws Exception {
        var filters = List.of(
                new FilterConfig(new ItemIdRule(1), new DisplayConfig(Color.RED, false)),
                new FilterConfig(new ItemNameRule("bandos-crossbow"), new DisplayConfig(Color.GREEN, false)),
                new FilterConfig(new ItemValueRule(1_000, Comparator.LT), new DisplayConfig(Color.BLUE, false)),
                new FilterConfig(new ItemQuantityRule(1_000, Comparator.LT), new DisplayConfig(Color.WHITE, false)),
                new FilterConfig(
                        new AndRule(
                                new ItemNameRule("Coins"),
                                new ItemQuantityRule(5, Comparator.EQ)
                        ),
                        new DisplayConfig(Color.WHITE.darker(), false)
                )
        );

        var ser = FilterConfig.toJson(filters);
        var deser = FilterConfig.fromJson(ser);

        if (!Objects.deepEquals(filters, deser)) {
            throw new Exception("serialized and deserialized rules do not match");
        }
    }
}
