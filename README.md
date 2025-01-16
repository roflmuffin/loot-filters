# `riktenx/loot-filters`

Work in progress.

`loot-filters` is a general-purpose, highly configurable plugin that controls when and how ground item text overlays are
displayed. It is essentially an extended implementation of the builtin ground items plugin with additional config.

This plugin is mutually exclusive with the ground items builtin, which should be disabled when using this one.

## configuration

The base configuration is largely mirrored from that of the ground items builtin, supporting the following:
* general "highlight" list
* general "hide" list
* configurable item value tiers (insane, high, medium, low)

## filter syntax

The primary addition of this plugin is support for scriptable filters.

syntax description todo
