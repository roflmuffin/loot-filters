package com.lootfilters;

import com.lootfilters.lang.Lexer;
import com.lootfilters.lang.Parser;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testSingleRule() throws Exception {
        var rsc = this.getClass().getResourceAsStream("parser-test.rs2f");
        var input = new String(rsc.readAllBytes());

        var tokens = new Lexer(input).tokenize();
        var parser = new Parser(tokens);
        var actual = parser.parse();
    }
}
