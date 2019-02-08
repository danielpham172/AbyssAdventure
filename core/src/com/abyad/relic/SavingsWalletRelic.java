package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;

public class SavingsWalletRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(SavingsWalletRelic.class);
	}
	
	
	public SavingsWalletRelic() {
		super("SAVINGS WALLET", "Gain gold at a slow constant rate", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("SAVINGS_WALLET"));
	}

	@Override
	public void goOnCooldown() {
		float factor = 1.0f / ((getCount() + 3) * 0.25f);
		factor = (float)Math.pow(factor, 0.8);
		cooldown = (int)(1200 * factor);
	}
	
	@Override
	public void onPassive(PlayerCharacter player) {
		player.modifyGold(1);
		goOnCooldown();
	}

	@Override
	public int getPriority() {
		return priorityNumber;
	}

}
