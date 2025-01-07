package com.lootfilters.lang;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Token {
    public enum Type {
        WHITESPACE,
        IF,
        META,
        COLON,
        COMMA,
        TRUE, FALSE,
        IDENTIFIER,
        LITERAL_INT, LITERAL_STRING,
        ASSIGN,
        OP_EQ, OP_GT, OP_LT, OP_GTEQ, OP_LTEQ, OP_AND, OP_OR,
        EXPR_START, EXPR_END,
        BLOCK_START, BLOCK_END,
        LIST_START, LIST_END,
        STMT_END,
    }

    public static Token Static(Type type) { return new Token(type, null); }
    public static Token IntLiteral(String value) { return new Token(Type.LITERAL_INT, value); }
    public static Token StringLiteral(String value) { return new Token(Type.LITERAL_STRING, value); }
    public static Token Identifier(String value) { return new Token(Type.IDENTIFIER, value); }

    public static final Token Equals = new Token(Type.OP_EQ, null);
    public static final Token GreaterThan = new Token(Type.OP_GT, null);
    public static final Token LessThan = new Token(Type.OP_LT, null);
    public static final Token GreaterThanOrEqual = new Token(Type.OP_GTEQ, null);
    public static final Token LessThanOrEqual = new Token(Type.OP_LTEQ, null);
    public static final Token And = new Token(Type.OP_AND, null);
    public static final Token Or = new Token(Type.OP_OR, null);
    public static final Token StartExpr = new Token(Type.EXPR_START, null);
    public static final Token EndExpr = new Token(Type.EXPR_END, null);
    public static final Token EndStatement = new Token(Type.STMT_END, null);
    public static final Token Whitespace = new Token(Type.WHITESPACE, null);

    Type type;
    String value;

    public boolean is(Type type) {
        return this.type == type;
    }

    public int expectInt() {
        if (type != Type.LITERAL_INT) {
            throw new ParseException("unexpected non-int token", this);
        }
        return Integer.parseInt(value);
    }

    public String expectString() {
        if (type != Type.LITERAL_STRING) {
            throw new ParseException("unexpected non-string token", this);
        }
        return value;
    }

    public boolean expectBoolean() {
        switch (type) {
            case TRUE: return true;
            case FALSE: return false;
            default:
                throw new ParseException("unexpected non-boolean token", this);
        }
    }

    @Override
    public String toString() {
        var str = "Token{type=" + type;
        return value != null && value.isEmpty()
                ? str + "}"
                : str + ",value=" + value + "}";
    }
}
