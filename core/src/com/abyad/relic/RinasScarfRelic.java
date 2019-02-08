package com.abyad.relic;

import com.abyad.sprite.AbstractSpriteSheet;

public class RinasScarfRelic extends Relic {

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(RinasScarfRelic.class);
	}
	
	
	public RinasScarfRelic() {
		super("RINA'S SCARF", "Increases attack speed a slight amount", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("RINA'S_SCARF"));
		addAttribute("ATTACK SPEED", 0.10f);
	}
	
	@Override
	public float getAttribute(String name) {
		return (super.getAttribute(name)) * (0.5f * (getCount() + 1));
	}

	@Override
	public int getPriority() {
		return priorityNumber;
	}
	
}
