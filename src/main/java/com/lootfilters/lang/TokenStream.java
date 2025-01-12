package com.lootfilters.lang;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * TokenStream wraps a list of Tokens to expose retrieval APIs suitable for parsing.
 */
@RequiredArgsConstructor
public class TokenStream {
    private final List<Token> tokens;

    public Token peek() {
        return tokens.stream()
                .filter(it -> !it.isWhitespace())
                .findFirst()
                .orElse(null);
    }

    public Token take(boolean includeWhitespace) {
        while (isNotEmpty()) {
            var next = tokens.remove(0);
            if (includeWhitespace || !next.isWhitespace()) {
                return next;
            }
        }
        return null;
    }

    public Token take() {
        return take(false);
    }

    public Token takeExpect(Token.Type expect, boolean includeWhitespace) {
        if (tokens.isEmpty()) {
            throw new ParseException("unexpected end of token stream");
        }

        var first = take(includeWhitespace);
        if (!first.is(expect)) {
            throw new ParseException("unexpected non-" + expect + " token", first);
        }
        return first;
    }

    public Token takeExpect(Token.Type expect) {
        if (tokens.isEmpty()) {
            throw new ParseException("unexpected end of token stream");
        }

        var first = take(false);
        if (!first.is(expect)) {
            throw new ParseException("unexpected non-" + expect + " token", first);
        }
        return first;
    }

    public Token takeExpectLiteral() {
        var first = take();
        if (!first.is(Token.Type.LITERAL_INT)
                && !first.is(Token.Type.LITERAL_STRING)
                && !first.is(Token.Type.TRUE)
                && !first.is(Token.Type.FALSE)) {
            throw new ParseException("unexpected non-literal token", first);
        }
        return first;
    }

    /**
     * Take a complete line from the stream, preserving whitespace, and EXCLUDING the newline at the end, which is
     * discarded.
     */
    public List<Token> takeLine() {
        var line = new ArrayList<Token>();
        while (!tokens.isEmpty()) {
            var next = tokens.remove(0);
            if (next.is(Token.Type.NEWLINE)) {
                return line;
            }
            line.add(next);
        }
        return line;
    }

    public void takeOptional(Token.Type type) {
        if (peek().is(type)) {
            take();
        }
    }

    public TokenStream takeBlock() {
        var tokens = new ArrayList<Token>();
        var state = new Stack<Token>();
        if (!peek().is(Token.Type.BLOCK_START)) {
            throw new ParseException("unexpected token", peek());
        }

        while (isNotEmpty()) {
            var next = take();
            if (next.is(Token.Type.BLOCK_START)) {
                state.push(next);
                if (!tokens.isEmpty()) { // inner block start, preserve it
                    tokens.add(next);
                }
            } else if (next.is(Token.Type.BLOCK_END)) {
                if (!state.isEmpty()) {
                    state.pop();
                    if (!state.isEmpty()) { // STILL not empty = inner block end, preserve it
                        tokens.add(next);
                    }
                } else {
                    throw new ParseException("unbalanced block: more { than }");
                }
            } else {
                tokens.add(next);
            }

            if (state.isEmpty()) { // end of original block
                return new TokenStream(tokens);
            }
        }
        if (!state.isEmpty()) {
            throw new ParseException("unbalanced block: more { than }");
        }

        return new TokenStream(tokens);
    }

    // TODO: replace usage w/ walkExpression(EXPR_START, EXPR_END, ...)
    public void walkExpression(Consumer<Token> consumer) throws ParseException {
        var state = new Stack<Token>();
        if (!peek().is(Token.Type.EXPR_START)) {
            throw new ParseException("unexpected start of expression", peek());
        }

        while (isNotEmpty()) {
            var next = take();
            if (next.is(Token.Type.EXPR_START)) {
                state.push(next);
            } else if (next.is(Token.Type.EXPR_END)) {
                if (!state.isEmpty()) {
                    state.pop();
                } else {
                    throw new ParseException("unbalanced expression: more ) than (");
                }
            }

            consumer.accept(next);
            if (state.isEmpty()) { // balanced expression
                return;
            }
        }
        if (!state.isEmpty()) {
            throw new ParseException("unbalanced expression: more ( than )");
        }
    }

    /**
     * Traverse an expression within the stream.
     * The traversal will verify that the expression in the stream is balanced. The caller can and most likely will
     * still maintain their own operator stack, but it won't require balance checks.
     */
    public void walkExpression(Token.Type start, Token.Type end, Consumer<Token> consumer) {
        var state = new Stack<Token>();
        if (!peek().is(start)) {
            throw new ParseException("unexpected start of expression", peek());
        }

        while (isNotEmpty()) {
            var next = take();
            if (next.is(start)) {
                state.push(next);
            } else if (next.is(end)) {
                if (!state.isEmpty()) {
                    state.pop();
                } else {
                    throw new ParseException("unbalanced expression");
                }
            }

            consumer.accept(next);
            if (state.isEmpty()) { // balanced expression
                return;
            }
        }
        if (!state.isEmpty()) {
            throw new ParseException("unbalanced expression");
        }
    }

    public TokenStream take(Token.Type start, Token.Type end, boolean preserveEnclosing) {
        var inner = new ArrayList<Token>();
        walkExpression(start, end, inner::add);
        if (!preserveEnclosing) {
            inner.remove(0);
            inner.remove(inner.size() - 1);
        }
        return new TokenStream(inner);
    }

    public TokenStream take(Token.Type start, Token.Type end) {
        return take(start, end, false);
    }

    public List<Token> all() {
        var ret = new ArrayList<Token>();
        while (isNotEmpty()) {
            ret.add(take());
        }
        return ret;
    }

    public List<TokenStream> takeArgList() {
        var args = new ArrayList<TokenStream>();
        var current = new ArrayList<Token>();
        var expr = take(Token.Type.EXPR_START, Token.Type.EXPR_END);
        while (expr.isNotEmpty()) {
            var head = expr.peek();
            if (head.is(Token.Type.COMMA)) {
                if (current.isEmpty()) {
                    throw new ParseException("empty argument");
                } else {
                    args.add(new TokenStream(current));
                    current = new ArrayList<>();
                    expr.take();
                }
            } else if (head.is(Token.Type.EXPR_START)) {
                var nestedExpr = expr.take(Token.Type.EXPR_START, Token.Type.EXPR_END, true);
                current.addAll(nestedExpr.all());
            } else {
                current.add(expr.take());
            }
        }

        if (!current.isEmpty()) {
            args.add(new TokenStream(current));
        }
        return args;
    }

    public boolean isNotEmpty() {
        return tokens.stream()
                .anyMatch(it -> !it.isWhitespace());
    }
}
