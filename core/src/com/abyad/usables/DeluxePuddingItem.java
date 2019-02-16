package com.abyad.usables;

import com.abyad.sprite.AbstractSpriteSheet;

public class DeluxePuddingItem extends HealingItem{

	public DeluxePuddingItem() {
		super("DELUXE PUDDING", 4, AbstractSpriteSheet.spriteSheets.get("INVENTORY_ITEMS").getSprite("DELUXE_PUDDING"));
	}
}
