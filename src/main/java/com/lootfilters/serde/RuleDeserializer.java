package com.lootfilters.serde;

import com.google.gson.*;
import com.lootfilters.rule.Rule;
import com.lootfilters.rule.*;

import java.lang.reflect.Type;

public class RuleDeserializer implements JsonDeserializer<Rule> {
    @Override
    public Rule deserialize(JsonElement elem, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        var object = elem.getAsJsonObject();
        var discriminator = object.get("discriminator").getAsString();
        switch (discriminator) {
            case "item_id":
                return new Gson().fromJson(object, ItemIdRule.class);
            case "item_name":
                return new Gson().fromJson(object, ItemNameRule.class);
            case "item_value":
                return new Gson().fromJson(object, ItemValueRule.class);
            case "item_quantity":
                return new Gson().fromJson(object, ItemQuantityRule.class);
            case "and":
                return deserializeInner(object, AndRule.class);
            case "or":
                return deserializeInner(object, OrRule.class);
            default:
                throw new JsonParseException("unknown rule type " + discriminator);
        }
    }

    private Rule deserializeInner(JsonElement elem, Type type) throws JsonParseException {
        return new GsonBuilder()
                .registerTypeAdapter(Rule.class, this)
                .create()
                .fromJson(elem, type);
    }
}
