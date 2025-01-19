# `riktenx/loot-filters`

![](https://github.com/riktenx/loot-filters/blob/main/readme-images/header.png)

Loot Filters (AKA "Improved Ground Items") is an opinionated extension on top of Runelite's built-in Ground Items
plugin.

## Overview

This plugin replicates the majority of the experience offered by the base version:
* Text overlay for ground items
* Configure display colors based on item value
* Lootbeams
* Configure highlighted and hidden item lists in real-time using a configurable hotkey

## Additions

The plugin provides the following extensions over the base Ground Items experience:
* Streamlined text overlay
* Streamlined configuration
* **Scriptable rules language, A.K.A. "Loot Filters" for fine-grained control over display and color settings for items
  on the ground.**
  * Save/load multiple filters
  * Import current config (highlight, hide, and value rules) into a new filter
  * Automatically toggle active filter based on your position in the game world (e.g. for bossing)