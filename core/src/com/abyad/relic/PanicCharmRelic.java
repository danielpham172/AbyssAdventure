package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;

public class PanicCharmRelic extends Relic{

	public PanicCharmRelic() {
		super("PANIC CHARM", "Increases speed when attacked", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("PANIC_CHARM"));
	}

	@Override
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		defense.addStatusEffect("SPEED", 0.30f);
	}
}
