package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;

public class PanicCharmRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(PanicCharmRelic.class);
	}
	
	
	public PanicCharmRelic() {
		super("PANIC CHARM", "Increases speed when attacked", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("PANIC_CHARM"));
	}

	@Override
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		defense.addStatusEffect("SPEED-MAX", 0.20f + (getCount() * 0.10f));
	}

	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
