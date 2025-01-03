package com.lootfilters;

import com.lootfilters.lang.Lexer;
import com.lootfilters.lang.Parser;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testSingleRule() throws Exception {
        var input = new Lexer("if ((id:999 || id:451) && id:1111) {" +
                "color = \"ff00ff00\";" +
                "showLootbeam = true;" +
                "}").tokenize();

        var parser = new Parser(input);
        var actual = parser.parse();
    }
}
