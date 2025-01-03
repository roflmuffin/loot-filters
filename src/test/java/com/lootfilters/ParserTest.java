package com.lootfilters;

import com.lootfilters.lang.Lexer;
import com.lootfilters.lang.Parser;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testSingleRule() throws Exception {
        var input = new Lexer("if (value:>10000000) {\n" +
                "  color=\"ffff8000\";\n" +
                "  showLootbeam=true;\n" +
                "}\n" +
                "if (value:>1000000) {\n" +
                "  color=\"ffa335ee\";\n" +
                "  showLootbeam=true;\n" +
                "}\n" +
                "if (value:>100000) {\n" +
                "  color=\"ff0070dd\";\n" +
                "}\n" +
                "if (value:>10000) {\n" +
                "  color=\"ff1eff00\";\n" +
                "}").tokenize();

        var parser = new Parser(input);
        var actual = parser.parse();
    }
}
