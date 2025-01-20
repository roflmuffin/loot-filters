package com.lootfilters.rule;

public enum ValueTier {
    INSANE, HIGH, MEDIUM, LOW;

    @Override
    public String toString() {
        switch (this) {
            case INSANE: return "insane";
            case HIGH: return "high";
            case MEDIUM: return "medium";
            default: return "low";
        }
    }
}
