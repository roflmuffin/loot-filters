package com.lootfilters.serde;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.runelite.client.util.ColorUtil;

import java.awt.Color;
import java.lang.reflect.Type;

public class ColorDeserializer implements JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonElement elem, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        return ColorUtil.fromHex(elem.getAsString());
    }
}
