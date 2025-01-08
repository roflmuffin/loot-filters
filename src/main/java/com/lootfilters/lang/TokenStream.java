package com.lootfilters.lang;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class TokenStream {
    private final List<Token> tokens;

    public Token peek() {
        return tokens.stream()
                .filter(it -> !it.is(Token.Type.WHITESPACE))
                .findFirst()
                .orElse(null);
    }

    public Token take() {
        while (isNotEmpty()) {
            var next = tokens.remove(0);
            if (next.getType() != Token.Type.WHITESPACE) {
                return next;
            }
        }
        return null;
    }

    public void takeOptional(Token.Type type) {
        if (peek().is(type)) {
            take();
        }
    }

    public Token takeExpect(Token.Type expect) {
        if (tokens.isEmpty()) {
            throw new ParseException("unexpected end of token stream");
        }

        var first = take();
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

    // Traverse an expression within the stream.
    // walkExpression will verify the expression in the stream is balanced, the caller can and most likely will still
    // maintain their own operator stack, but it won't require balance checks.
    //
    // fix: if expressions were generic, this wouldn't be necessary because the traversal would be the same everywhere.
    // For now that is not the case, expressions have distinct syntaxes, except there's only the one which is for Rule.
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

    public boolean isNotEmpty() {
        return tokens.stream()
                .anyMatch(it -> !it.is(Token.Type.WHITESPACE));
    }
}
