package com.lootfilters.lang;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.lootfilters.util.TextUtil.isLegalIdent;
import static com.lootfilters.util.TextUtil.isNumeric;
import static com.lootfilters.util.TextUtil.isWhitespace;

@RequiredArgsConstructor
public class Lexer {
    private static final LinkedHashMap<String, Token.Type> STATICS = new LinkedHashMap<>() {{
        put("false", Token.Type.FALSE);
        put("true", Token.Type.TRUE);
        put("meta", Token.Type.META);
        put("if", Token.Type.IF);
        put("&&", Token.Type.OP_AND);
        put("||", Token.Type.OP_OR);
        put(">=", Token.Type.OP_GTEQ);
        put("<=", Token.Type.OP_LTEQ);
        put("==", Token.Type.OP_EQ);
        put(">", Token.Type.OP_GT);
        put("<", Token.Type.OP_LT);
        put(";", Token.Type.STMT_END);
        put(":", Token.Type.COLON);
        put("=", Token.Type.ASSIGN);
        put(",", Token.Type.COMMA);
        put("(", Token.Type.EXPR_START);
        put(")", Token.Type.EXPR_END);
        put("{", Token.Type.BLOCK_START);
        put("}", Token.Type.BLOCK_END);
        put("[", Token.Type.LIST_START);
        put("]", Token.Type.LIST_END);
    }};

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
