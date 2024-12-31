# `riktenx/loot-filters`

Work in progress.

`loot-filters` is a general-purpose, highly configurable plugin that controls when and how ground item text overlays are
displayed.

This plugin is effectively mutually exclusive with the builtin ground items plugin, which should be disabled when using
this alternative.

## filter syntax

todo

### example: item value tiers

The following filter syntax re-implements the item value display colors supported by the builtin ground items plugin:

```json
[
  {
    "rule": {
      "discriminator": "item_value", "rhs": 10000000, "cmp": "GT_EQ"
    },
    "display": {
      "color": "ffff8000", "showLootbeam": true
    }
  },
  {
    "rule": {
      "discriminator": "item_value", "rhs": 1000000, "cmp": "GT_EQ"
    },
    "display": {
      "color": "ffa335ee", "showLootbeam": true
    }
  },
  {
    "rule": {
      "discriminator": "item_value", "rhs": 100000, "cmp": "GT_EQ"
    },
    "display": {
      "color": "ff0070dd"
    }
  },
  {
    "rule": {
      "discriminator": "item_value", "rhs": 10000, "cmp": "GT_EQ"
    },
    "display": {
      "color": "ff1eff00"
    }
  },
  {
    "rule": {
      "discriminator": "item_quantity", "rhs": 0, "cmp": "GT"
    },
    "display": {
      "color": "ff1eff00"
    }
  }
]
```

## todo
* feature: filter language
* feature: ground items parity: recolor / add quantity to menu items
* feature: ground items parity: ownership filter
* feature: display config: global overrides
* feature?: display config: font size
* feature?: ground items parity: global "hide" list
* fix: reflow lootbeam display on filter config update