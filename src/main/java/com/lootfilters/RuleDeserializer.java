package com.lootfilters;

import com.google.gson.*;
import com.lootfilters.rule.*;

import java.lang.reflect.Type;

public class RuleDeserializer implements JsonDeserializer<Rule> {
    private final Gson defaultGson = new Gson();

    @Override
    public Rule deserialize(JsonElement elem, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        var object = elem.getAsJsonObject();
        var discriminator = object.get("discriminator").getAsString();
        switch (discriminator) {
            case "item_id":
                return defaultGson.fromJson(object, ItemIdRule.class);
            case "item_name":
                return defaultGson.fromJson(object, ItemNameRule.class);
            case "item_value":
                return defaultGson.fromJson(object, ItemValueRule.class);
            case "item_quantity":
                return defaultGson.fromJson(object, ItemQuantityRule.class);
            case "and":
                var deserializer = new GsonBuilder()
                        .registerTypeAdapter(Rule.class, this)
                        .create();
                return deserializer.fromJson(object, AndRule.class);
            default:
                throw new JsonParseException("unknown rule type " + discriminator);
        }
    }
}
