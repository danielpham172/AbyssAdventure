package com.abyad.relic;

import com.abyad.sprite.AbstractSpriteSheet;

public class PowerGloveRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(PowerGloveRelic.class);
	}
	
	
	public PowerGloveRelic() {
		super("POWER GLOVE", "Decreases speed loss when carrying items", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("POWER_GLOVE"));
		addAttribute("CARRY STRENGTH", 1.0f);
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
