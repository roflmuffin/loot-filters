package com.lootfilters;

public class Filter {
    private final Rule rule;
    private final int display;

    public Filter(Rule rule, int display) {
        this.rule = rule;
        this.display = display;
    }
}
