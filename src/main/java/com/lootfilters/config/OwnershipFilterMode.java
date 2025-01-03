package com.lootfilters.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OwnershipFilterMode {
    ALL("All"),
    TAKEABLE("Takeable"),
    DROPS("Drops");

    private final String name;

    @Override
    public String toString()
    {
        return name;
    }
}
