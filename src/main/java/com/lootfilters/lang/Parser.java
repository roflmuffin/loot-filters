package com.lootfilters.lang;

import com.lootfilters.FilterConfig;
import com.lootfilters.rule.AndRule;
import com.lootfilters.rule.ItemIdRule;
import com.lootfilters.rule.ItemNameRule;
import com.lootfilters.rule.ItemQuantityRule;
import com.lootfilters.rule.ItemValueRule;
import com.lootfilters.rule.OrRule;
import com.lootfilters.rule.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    private final TokenStream tokens;
    private final List<FilterConfig> filters = new ArrayList<>();

    public Parser(List<Token> tokens) {
        this.tokens = new TokenStream(tokens);
    }

    public List<FilterConfig> parse() throws Exception {
        while (tokens.isNotEmpty()) {
            var first = tokens.takeFirst();
            if (first.getType() == Token.Type.IF) {
                parseFilterConfig();
            } else {
                throw new Exception("unhandled token " + first.getType());
            }
        }
        return filters;
    }

    // 5*4*3+7
    // 543**7+
    // (5*4*3+7)
    // 543**7+
    // (5*4*(3+7))
    // 5437+**

    private void parseFilterConfig() {
        var operators = new Stack<Token>();
        var rulesPostfix = new ArrayList<Rule>();
        tokens.walkExpression(it -> {
            if (it.is(Token.Type.EXPR_START)) {
                operators.push(it);
            } else if (it.is(Token.Type.EXPR_END)) {
                while (!operators.isEmpty() && !operators.peek().is(Token.Type.EXPR_START)) {
                    var op = operators.pop();
                    if (op.is(Token.Type.OP_AND)) {
                        rulesPostfix.add(new AndRule(null));
                    } else if (op.is(Token.Type.OP_OR)) {
                        rulesPostfix.add(new OrRule(null));
                    }
                }
            } else if (it.is(Token.Type.OP_AND)) {
                operators.push(it);
            } else if (it.is(Token.Type.OP_OR)) {
                while (!operators.isEmpty() && operators.peek().is(Token.Type.OP_AND)) {
                    operators.pop();
                    rulesPostfix.add(new AndRule(null));
                }
                operators.push(it);
            } else if (it.is(Token.Type.IDENTIFIER)) {
                rulesPostfix.add(parseRule(it));
            } else {
                throw new ParseException("unexpected token in expression", it);
            }
        });

        while (!operators.isEmpty()) { // is this necessary? since parenthesis around overall expr are guaranteed
            var op = operators.pop();
            if (op.is(Token.Type.OP_AND)) {
                rulesPostfix.add(new AndRule(null));
            } else if (op.is(Token.Type.OP_OR)) {
                rulesPostfix.add(new OrRule(null));
            }
        }

        filters.add(new FilterConfig(buildRule(rulesPostfix), null));
    }

    private Rule parseRule(Token first) {
        tokens.takeExpectFirst(Token.Type.COLON); // grammar is always <id><colon><...>
        switch (first.getValue()) {
            case "id":
                return parseItemIdRule();
            case "name":
                return parseItemNameRule();
            case "quantity":
                return parseItemQuantityRule();
            case "value":
                return parseItemValueRule();
            default:
                throw new ParseException("unknown rule identifier", first);
        }
    }

    private ItemIdRule parseItemIdRule() {
        var id = tokens.takeExpectFirst(Token.Type.LITERAL_INT);
        return new ItemIdRule(Integer.parseInt(id.getValue()));
    }

    private ItemNameRule parseItemNameRule() {
        throw new ParseException("TODO");
    }

    private ItemQuantityRule parseItemQuantityRule() {
        throw new ParseException("TODO");
    }

    private ItemValueRule parseItemValueRule() {
        throw new ParseException("TODO");
    }

    private Rule buildRule(List<Rule> postfix) {
        var operands = new Stack<Rule>();
        for (var rule : postfix) {
            if (rule instanceof ItemIdRule
                    || rule instanceof ItemNameRule
                    || rule instanceof ItemQuantityRule
                    || rule instanceof ItemValueRule) {
                operands.push(rule);
            } else if (rule instanceof AndRule) {
                var left = operands.pop();
                var right = operands.pop();
                operands.push(new AndRule(left, right));
            } else if (rule instanceof OrRule) {
                var left = operands.pop();
                var right = operands.pop();
                operands.push(new OrRule(left, right));
            }
        }

        if (operands.size() != 1) {
            throw new ParseException("invalid rule postfix");
        }
        return operands.pop();
    }
}
