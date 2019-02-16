package com.abyad.relic;

import com.abyad.actor.attack.AttackData;
import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;

public class VampiricFangRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(VampiricFangRelic.class);
	}
	
	public VampiricFangRelic() {
		super("VAMPIRIC FANG", "Chance to heal when attacking", 0.04f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("VAMPIRIC_FANG"));
	}

	@Override
	public float getActivationRate() {
		return Math.min(super.getActivationRate() * (0.5f * (getCount() + 1)), 0.40f);
	}
	
	@Override
	public void onHit(PlayerCharacter player, AttackData attackType, HitEvent attack, AbstractEntity hit) {
		player.restoreHealth(1);
	}

	@Override
	public int getPriority() {
		return priorityNumber;
	}
	
}
