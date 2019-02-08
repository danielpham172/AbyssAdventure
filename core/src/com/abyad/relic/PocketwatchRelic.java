package com.abyad.relic;

import com.abyad.sprite.AbstractSpriteSheet;

public class PocketwatchRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(PocketwatchRelic.class);
	}
	
	
	public PocketwatchRelic() {
		super("POCKETWATCH", "Increases cast speed", 0.04f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("POCKETWATCH"));
		addAttribute("CAST SPEED", 0.15f);
	}
	
	@Override
	public float getAttribute(String name) {
		return super.getAttribute(name) * getCount();
	}

	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
