package com.abyad.usables;

import com.abyad.sprite.AbstractSpriteSheet;

public class UltimatePuddingItem extends HealingItem{

	public UltimatePuddingItem() {
		super("ULTIMATE PUDDING", 10, AbstractSpriteSheet.spriteSheets.get("INVENTORY_ITEMS").getSprite("ULTIMATE_PUDDING"));
	}
}
