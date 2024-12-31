package com.lootfilters.serde;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.awt.Color;
import java.lang.reflect.Type;

public class ColorDeserializer implements JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonElement elem, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        var argb = elem.getAsString();
        return new Color(Long.decode("0x" + argb).intValue(), true);
    }
}
