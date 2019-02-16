package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;

public class RubyPendantRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(RubyPendantRelic.class);
	}
	
	
	public RubyPendantRelic() {
		super("RUBY PENDANT", "Recover HP at a very slow rate", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("RUBY_PENDANT"));
	}

	@Override
	public void goOnCooldown() {
		float factor = 1.0f / ((getCount() + 4) * 0.2f);
		factor = (float)Math.pow(factor, 0.8);
		cooldown = (int)(14400 * factor);
	}
	
	@Override
	public void onPassive(PlayerCharacter player) {
		if (player.getHP() < player.getMaxHP()) {
			player.modifyHP(1);
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
