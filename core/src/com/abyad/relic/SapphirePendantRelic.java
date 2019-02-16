package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;

public class SapphirePendantRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(SapphirePendantRelic.class);
	}
	
	
	public SapphirePendantRelic() {
		super("SAPPHIRE PENDANT", "Recover Mana at a very slow rate", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("SAPPHIRE_PENDANT"));
	}

	@Override
	public void goOnCooldown() {
		float factor = 1.0f / ((getCount() + 4) * 0.2f);
		factor = (float)Math.pow(factor, 0.8);
		cooldown = (int)(3600 * factor);
	}
	
	@Override
	public void onPassive(PlayerCharacter player) {
		if (player.getMana() < player.getMaxMana()) {
			player.addPartialMana(1);
			goOnCooldown();
		}
		else {
			goOnCooldown();
			cooldown /= 4;
		}
	}

	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
