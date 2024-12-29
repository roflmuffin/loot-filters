package com.lootfilters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lootfilters.rule.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterSerdeTest {
    public static void main(String[] args) throws Exception {
        var filters = List.of(
                new Filter(new ItemIdRule(null, 1), new DisplayConfig(Color.RED, false)),
                new Filter(new ItemNameRule(null, "bandos-crossbow"), new DisplayConfig(Color.GREEN, false)),
                new Filter(new ItemValueRule(null, 1_000, Comparator.LT), new DisplayConfig(Color.BLUE, false)),
                new Filter(new ItemQuantityRule(null, 1_000, Comparator.LT), new DisplayConfig(Color.WHITE, false)),
                new Filter(
                        new AndRule(null,
                                new ItemNameRule(null, "Coins"),
                                new ItemQuantityRule(null, 5, Comparator.EQ)),
                        new DisplayConfig(Color.WHITE.darker(), false)
                )
        );

        var gson = new Gson();
        var ser = gson.toJson(filters);

        var deserializer = new GsonBuilder()
                .registerTypeAdapter(Rule.class, new RuleDeserializer())
                .create();
        var deser = deserializer.fromJson(ser, new TypeToken<ArrayList<Filter>>() {}.getType());

        if (!Objects.deepEquals(filters, deser)) {
            throw new Exception("serialized and deserialized rules do not match");
        }
    }
}
