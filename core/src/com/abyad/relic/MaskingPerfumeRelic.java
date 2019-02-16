package com.abyad.relic;

import com.abyad.sprite.AbstractSpriteSheet;

public class MaskingPerfumeRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(MaskingPerfumeRelic.class);
	}
	
	
	public MaskingPerfumeRelic() {
		super("MASKING PERFUME", "Decreases range enemies can see you", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("MASKING_PERFUME"));
		addAttribute("TAUNT RANGE", -0.05f);
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
