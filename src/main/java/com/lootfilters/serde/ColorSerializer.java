package com.lootfilters.serde;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.awt.Color;
import java.lang.reflect.Type;

public class ColorSerializer implements JsonSerializer<Color> {
    @Override
    public JsonElement serialize(Color color, Type type, JsonSerializationContext ctx) {
        var argb = String.format("%02x", color.getAlpha())
                + String.format("%02x", color.getRed())
                + String.format("%02x", color.getGreen())
                + String.format("%02x", color.getBlue());
        return new JsonPrimitive(argb);
    }
}
