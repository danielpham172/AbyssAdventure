package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.TreasureChest;
import com.abyad.actor.mapobjects.items.GoldItem;
import com.abyad.sprite.AbstractSpriteSheet;

public class LockpickRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(LockpickRelic.class);
	}
	
	
	public LockpickRelic() {
		super("LOCKPICK", "Occasionally gain additional gold from chests", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("LOCKPICK"));
	}

	@Override
	public void onChestOpen(PlayerCharacter player, TreasureChest chest) {
		int tries = 2 + getCount();
		for (int i = 0; i < tries; i++) {
			if (Math.random() < 0.20) {
				chest.addItem(new GoldItem());
			}
		}
	}
	
	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
