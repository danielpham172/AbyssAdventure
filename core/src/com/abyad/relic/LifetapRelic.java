package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;

public class LifetapRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(LifetapRelic.class);
	}
	
	public LifetapRelic() {
		super("LIFETAP", "Occasionally restore Mana when taking damage", 0.15f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("LIFETAP"));
	}

	@Override
	public float getActivationRate() {
		return super.getActivationRate() * (0.5f * (getCount() + 1));
	}
	
	@Override
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		player.addPartialMana(1);
	}
	
	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
