package com.lootfilters;

import java.io.IOException;

import static com.lootfilters.util.TextUtil.normalizeCrlf;

public class TestUtil {
    private TestUtil() {}

    public static String loadTestResource(String resource) throws IOException {
        try (var r = TestUtil.class.getResourceAsStream(resource)) {
            assert r != null;
            return normalizeCrlf(new String(r.readAllBytes()));
        }
    }
}
