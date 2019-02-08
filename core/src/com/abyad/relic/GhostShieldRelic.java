package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;

public class GhostShieldRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(GhostShieldRelic.class);
	}
	
	public GhostShieldRelic() {
		super("GHOST SHIELD", "Extends invulnerability time after getting hit", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("GHOST_SHIELD"));
	}

	@Override
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		float invulnScale = (float)Math.min(1.0f + ((0.075f) * (getCount() + 1)), 5.0f);
		defense.setInvulnModifier(defense.getInvulnModifier() * invulnScale);
	}

	@Override
	public int getPriority() {
		return priorityNumber;
	}
	
}
