package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;

public class TonWeightRelic extends Relic{

	public TonWeightRelic() {
		super("TON WEIGHT", "Lower knockback by 15%", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("TON_WEIGHT"));
	}

	@Override
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		defense.setKnockbackVelocity(defense.getKnockbackVelocity().scl(0.85f));
	}
}
