package com.lootfilters.lang;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lootfilters.util.TextUtil.isLegalIdent;
import static com.lootfilters.util.TextUtil.isNumeric;
import static com.lootfilters.util.TextUtil.isWhitespace;
import static java.util.Map.entry;

@RequiredArgsConstructor
public class Lexer {
    private static final Map<String, Token.Type> STATICS = Map.ofEntries(
            entry("(", Token.Type.EXPR_START),
            entry(")", Token.Type.EXPR_END),
            entry("==", Token.Type.OP_EQ),
            entry(">", Token.Type.OP_GT),
            entry("<", Token.Type.OP_LT),
            entry(">=", Token.Type.OP_GTEQ),
            entry("<=", Token.Type.OP_LTEQ),
            entry("&&", Token.Type.OP_AND),
            entry("||", Token.Type.OP_OR),
            entry(";", Token.Type.STMT_END),
            entry("true", Token.Type.TRUE),
            entry("false", Token.Type.FALSE),
            entry("if", Token.Type.IF),
            entry(":", Token.Type.COLON),
            entry("{", Token.Type.BLOCK_START),
            entry("}", Token.Type.BLOCK_END),
            entry("=", Token.Type.ASSIGN)
    );

    private final String input;
    private final List<Token> tokens = new ArrayList<>();
    private int offset = 0;

    public List<Token> tokenize() throws Exception {
        while (offset < input.length()) {
            if (tokenizeStatic()) {
                continue;
            }

            var ch = input.charAt(offset);
            if (isWhitespace(ch)) {
                tokenizeWhitespace();
            } else if (isNumeric(ch)) {
                tokenizeLiteralInt();
            } else if (ch == '"') {
                tokenizeLiteralString();
            } else if (isLegalIdent(ch)) {
                tokenizeIdentifier();
            } else {
                throw new Exception("unrecognized character '" + ch + "'");
            }
        }

        return tokens;
    }

    private boolean tokenizeStatic() {
        for (var entry : STATICS.entrySet()) {
            var value = entry.getKey();
            var type = entry.getValue();
            if (input.startsWith(value, offset)) {
                tokens.add(Token.Static(type));
                offset += value.length();
                return true;
            }
        }
        return false;
    }

    private void tokenizeWhitespace() {
        for (int i = offset; i < input.length(); ++i) {
            if (!isWhitespace(input.charAt(i))) {
                tokens.add(Token.Whitespace);
                offset += i - offset;
                return;
            }
        }
        tokens.add(Token.Whitespace);
        offset = input.length();
    }

    private void tokenizeLiteralInt() {
        for (int i = offset; i < input.length(); ++i) {
            if (!isNumeric(input.charAt(i))) {
                var literal = input.substring(offset, i);
                tokens.add(Token.IntLiteral(literal));
                offset += literal.length();
                return;
            }
        }
        tokens.add(Token.IntLiteral(input.substring(offset)));
        offset = input.length();
    }

    private void tokenizeLiteralString() throws Exception {
        for (int i = offset+1; i < input.length(); ++i) {
            if (input.charAt(i) == '"') {
                var literal = input.substring(offset+1, i);
                tokens.add(Token.StringLiteral(literal));
                offset += literal.length() + 2; // for quotes, which the captured literal omits
                return;
            }
        }
        throw new Exception("unterminated string literal");
    }

    private void tokenizeIdentifier() {
        for (int i = offset; i < input.length(); ++i) {
            if (!isLegalIdent(input.charAt(i))) {
                var ident = input.substring(offset, i);
                tokens.add(Token.Identifier(ident));
                offset += ident.length();
                return;
            }
        }
        tokens.add(Token.Identifier(input.substring(offset)));
        offset = input.length();
    }
}
