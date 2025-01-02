package com.lootfilters.lang;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Token {
    public enum Type {
        WHITESPACE,
        TRUE,
        FALSE,
        IDENTIFIER,
        LITERAL_INT,
        LITERAL_STRING,
        OP_EQ, OP_GT, OP_LT, OP_GTEQ, OP_LTEQ,
        OP_AND, OP_OR,
        EXPR_START, EXPR_END,
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
}
