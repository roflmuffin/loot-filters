package com.lootfilters.lang;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
// TODO: this code is so fucking bad
public class Lexer {
    private final String input;

    // do this:
    // private final Map<String, Token.Type> STATICS = Map.of("(", Token.Type.EXPR_START)); // etc.
    // first thing tokenize() should do is check that, THEN try to figure out literals/dynamic tokens

    // terribly written, making tokens/offset members will improve
    // switch + if is improvised nonsense
    public List<Token> tokenize() throws Exception {
        var tokens = new ArrayList<Token>();
        var offset = 0;
        while (offset < input.length()) {
            var ch = input.charAt(offset);
            switch (ch) {
                case ' ':
                case '\t':
                case '\n':
                    var ws = tokenizeWhitespace(offset);
                    tokens.add(ws);
                    offset += ws.getValue().length();
                    continue;
                case '(':
                    tokens.add(new Token(Token.Type.EXPR_START, "("));
                    ++offset;
                    continue;
                case ')':
                    tokens.add(new Token(Token.Type.EXPR_END, ")"));
                    ++offset;
                    continue;
                case '"':
                    var ls = tokenizeLiteralString(offset);
                    tokens.add(ls);
                    offset += ls.getValue().length()+2;
                    continue;
                case ';':
                    tokens.add(new Token(Token.Type.STMT_END, ";"));
                    ++offset;
                    continue;
                case '>':
                    tokens.add(new Token(Token.Type.OPERATOR_GT, ">"));
                    ++offset;
                    continue;
                case '<':
                    tokens.add(new Token(Token.Type.OPERATOR_LT, "<"));
                    ++offset;
                    continue;
            }
            if (input.startsWith("&&", offset)) {
               tokens.add(new Token(Token.Type.OPERATOR_AND, "&&"));
               offset += 2;
            } else if (input.startsWith("||", offset)) {
                tokens.add(new Token(Token.Type.OPERATOR_OR, "||"));
                offset += 2;
            } else if (input.startsWith("true", offset)) {
                tokens.add(new Token(Token.Type.LITERAL_BOOL, "true"));
                offset += 4;
            } else if (input.startsWith("false", offset)) {
                tokens.add(new Token(Token.Type.LITERAL_BOOL, "false"));
                offset += 5;
            } else if (ch >= '0' && ch <= '9') {
                var tok = tokenizeLiteralInt(offset);
                tokens.add(tok);
                offset += tok.getValue().length();
            } else if (input.startsWith("RULE", offset)) {
                tokens.add(new Token(Token.Type.KEYWORD, "RULE"));
                offset += 4;
            } else if (input.startsWith("CFG", offset)) {
                tokens.add(new Token(Token.Type.KEYWORD, "CFG"));
                offset += 4;
            } else if (input.startsWith("==", offset)) {
                tokens.add(new Token(Token.Type.OPERATOR_EQ, "=="));
                offset += 2;
            } else if (input.startsWith(">=", offset)) {
                tokens.add(new Token(Token.Type.OPERATOR_GTEQ, ">="));
                offset += 2;
            } else if (input.startsWith("<=", offset)) {
                tokens.add(new Token(Token.Type.OPERATOR_LTEQ, "<="));
                offset += 2;
            } else {
                var tok = tokenizeIdentifier(offset);
                tokens.add(tok);
                offset += tok.getValue().length();
            }
        }
        return tokens;
    }

    private Token tokenizeWhitespace(int start) {
        for (int i = start; i < input.length(); ++i) {
            if (!isWhitespace(input.charAt(i))) {
                return new Token(Token.Type.WHITESPACE, input.substring(start, i));
            }
        }
        return new Token(Token.Type.WHITESPACE, input.substring(start));
    }

    private Token tokenizeLiteralInt(int start) {
        for (int i = start; i < input.length(); ++i) {
            if (!isNumeric(input.charAt(i))) {
                return new Token(Token.Type.LITERAL_INT, input.substring(start, i));
            }
        }
        return new Token(Token.Type.LITERAL_INT, input.substring(start));
    }

    private Token tokenizeLiteralString(int start) throws Exception {
        for (int i = start+1; i < input.length(); ++i) {
            if (input.charAt(i) == '"') {
                var literal = input.substring(start+1, i);
                return new Token(Token.Type.LITERAL_STRING, literal);
            }
        }
        throw new Exception("unterminated string literal");
    }

    private Token tokenizeIdentifier(int start) throws Exception {
        for (int i = start; i < input.length(); ++i) {
            if (!isLegal(input.charAt(i))) {
                return new Token(Token.Type.IDENTIFIER, input.substring(start, i));
            }
        }
        return new Token(Token.Type.IDENTIFIER, input.substring(start));
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n';
    }

    private boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    private boolean isLegal(char c) { // for identifiers
        return c == '_' || isAlpha(c) || isNumeric(c);
    }
}
