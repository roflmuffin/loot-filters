package com.lootfilters;

import com.lootfilters.lang.Lexer;
import com.lootfilters.lang.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LexerTest {
    @Test
    public void testTokenize() throws Exception {
        // i don't even think i like this
        var input = "RULE ( " +
                "id(451) && " +
                "name(\"bandos crossbow,legends cape,superior dragon bones\") || " +
                "count(1) " +
                ");";
        var expect = List.of(
                Token.Identifier("RULE"),
                Token.Whitespace,
                Token.StartExpr,
                Token.Whitespace,
                Token.Identifier("id"),
                Token.StartExpr,
                Token.IntLiteral("451"),
                Token.EndExpr,
                Token.Whitespace,
                Token.And,
                Token.Whitespace,
                Token.Identifier("name"),
                Token.StartExpr,
                Token.StringLiteral("bandos crossbow,legends cape,superior dragon bones"),
                Token.EndExpr,
                Token.Whitespace,
                Token.Or,
                Token.Whitespace,
                Token.Identifier("count"),
                Token.StartExpr,
                Token.IntLiteral("1"),
                Token.EndExpr,
                Token.Whitespace,
                Token.EndExpr,
                Token.EndStatement
        );

        var lexer = new Lexer(input);
        var actual = lexer.tokenize();
        assertEquals(expect, actual);
    }
}
