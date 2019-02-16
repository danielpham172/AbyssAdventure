package com.abyad.relic;

import com.abyad.sprite.AbstractSpriteSheet;

public class PowerMagnetRelic extends Relic {

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(PowerMagnetRelic.class);
	}
	
	
	public PowerMagnetRelic() {
		super("POWER MAGNET", "Increases pickup range of Hearts, Crystals, and Gold", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("POWER_MAGNET"));
		addAttribute("PICKUP RANGE", 0.08f);
	}
	
	@Override
	public float getAttribute(String name) {
		return (super.getAttribute(name)) * (0.40f * (getCount() + 2.5f));
	}

	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
