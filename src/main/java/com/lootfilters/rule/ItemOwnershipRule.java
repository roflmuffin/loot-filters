package com.lootfilters.rule;

import com.lootfilters.LootFiltersPlugin;
import com.lootfilters.config.OwnershipFilterMode;
import lombok.EqualsAndHashCode;
import net.runelite.api.TileItem;

@EqualsAndHashCode(callSuper = false)
public class ItemOwnershipRule extends Rule {
    private final OwnershipFilterMode mode;

    protected ItemOwnershipRule(OwnershipFilterMode mode) {
        super("item_ownership");
        this.mode = mode;
    }

    @Override
    public boolean test(LootFiltersPlugin plugin, TileItem item) {
        // Note in this context, returning false means "don't match because it PASSES the ownership filter, we'll let
        // other rules decide".
        var o = item.getOwnership();
        switch (mode) {
            case TAKEABLE:
                return o == TileItem.OWNERSHIP_OTHER;
            case DROPS:
                return o != TileItem.OWNERSHIP_SELF && o != TileItem.OWNERSHIP_GROUP;
            default:
                return false;
        }
    }
}
