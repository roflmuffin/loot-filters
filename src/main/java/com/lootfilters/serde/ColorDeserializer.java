package com.lootfilters.serde;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.awt.Color;
import java.lang.reflect.Type;

import static com.lootfilters.util.TextUtil.parseArgb;

public class ColorDeserializer implements JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonElement elem, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        return parseArgb(elem.getAsString());
    }
}
