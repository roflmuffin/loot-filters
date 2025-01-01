package com.lootfilters.lang;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Token {
    public enum Type {
        WHITESPACE,
        KEYWORD,
        IDENTIFIER,
        LITERAL_INT, LITERAL_BOOL, LITERAL_STRING,
        OPERATOR_EQ, OPERATOR_GT, OPERATOR_LT, OPERATOR_GTEQ, OPERATOR_LTEQ,
        OPERATOR_AND, OPERATOR_OR,
        EXPR_START, EXPR_END,
        STMT_END,
    }

    // individual keywords should probably be their own type, that will probably make syntax inspection easier
    public static Token Whitespace(String value) { return new Token(Token.Type.WHITESPACE, value); }
    public static Token Keyword(String value) { return new Token(Type.KEYWORD, value); }
    public static Token Identifier(String value) { return new Token(Type.IDENTIFIER, value); }
    public static Token IntLiteral(String value) { return new Token(Type.LITERAL_INT, value); }
    public static Token BoolLiteral(String value) { return new Token(Type.LITERAL_BOOL, value); }
    public static Token StringLiteral(String value) { return new Token(Type.LITERAL_STRING, value); }

    // no point in any of these having value set
    public static final Token Equals = new Token(Type.OPERATOR_EQ, "==");
    public static final Token GreaterThan = new Token(Type.OPERATOR_GT, ">");
    public static final Token LessThan = new Token(Type.OPERATOR_LT, "<");
    public static final Token GreaterThanOrEqual = new Token(Type.OPERATOR_GTEQ, ">");
    public static final Token LessThanOrEqual = new Token(Type.OPERATOR_LTEQ, "<");
    public static final Token And = new Token(Type.OPERATOR_AND, "&&");
    public static final Token Or = new Token(Type.OPERATOR_OR, "||");
    public static final Token StartExpr = new Token(Type.EXPR_START, "(");
    public static final Token EndExpr = new Token(Type.EXPR_END, ")");
    public static final Token EndStatement = new Token(Type.STMT_END, ";");

    Type type;
    String value;
}
