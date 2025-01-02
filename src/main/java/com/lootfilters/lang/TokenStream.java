package com.lootfilters.lang;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class TokenStream {
    private final List<Token> tokens;

    public Token peekFirst() {
        return tokens.stream()
                .filter(it -> !it.is(Token.Type.WHITESPACE))
                .findFirst()
                .orElse(null);
    }

    public Token takeFirst() {
        while (isNotEmpty()) {
            var next = tokens.remove(0);
            if (next.getType() != Token.Type.WHITESPACE) {
                return next;
            }
        }
        return null;
    }

    public Token takeExpectFirst(Token.Type expect) {
        var first = takeFirst();
        if (!first.is(expect)) {
            throw new ParseException("expected token type " + first.getType(), first);
        }
        return first;
    }

    // Traverse an expression within the stream.
    // walkExpression will verify the expression in the stream is balanced, the caller can and most likely will still
    // maintain their own operator stack, but it won't require balance checks.
    //
    // fix: if expressions were generic, this wouldn't be necessary because the traversal would be the same everywhere.
    // For now that is not the case, expressions have distinct syntaxes, except there's only the one which is for Rule.
    public void walkExpression(Consumer<Token> consumer) throws ParseException {
        var state = new Stack<Token>();
        if (!peekFirst().is(Token.Type.EXPR_START)) {
            throw new ParseException("unexpected start of expression", peekFirst());
        }

        while (isNotEmpty()) {
            var next = takeFirst();
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
        return !tokens.isEmpty();
    }
}
