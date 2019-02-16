package com.abyad.usables;

import com.abyad.sprite.AbstractSpriteSheet;

public class PuddingItem extends HealingItem {

	public PuddingItem() {
		super("PUDDING", 1, AbstractSpriteSheet.spriteSheets.get("INVENTORY_ITEMS").getSprite("PUDDING"));
	}

}
