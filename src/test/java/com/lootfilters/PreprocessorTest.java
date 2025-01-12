package com.lootfilters;

import com.lootfilters.lang.Preprocessor;
import org.junit.Test;

import static com.lootfilters.TestUtil.loadTestResource;
import static org.junit.Assert.assertEquals;

public class PreprocessorTest {
    @Test
    public void testPreprocess() throws Exception {
        var input = loadTestResource("preprocessor-test-input.rs2f");
        var expect = loadTestResource("preprocessor-test-expect.rs2f");

        var preprocessor = new Preprocessor(input);
        var actual = preprocessor.preprocess();
        assertEquals(expect, actual);
    }
}
