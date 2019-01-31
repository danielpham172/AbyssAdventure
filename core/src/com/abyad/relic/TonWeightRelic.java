package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;

public class TonWeightRelic extends Relic{

	public TonWeightRelic() {
		super("TON WEIGHT", "Lowers knockback by 15%", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("TON_WEIGHT"));
	}

	@Override
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		float kbScale = 1.0f;
		for (int i = 0; i < getCount(); i++) {
			kbScale *= 0.85f;
		}
		defense.setKnockbackVelocity(defense.getKnockbackVelocity().scl(kbScale));
	}
	
	@Override
	public int getPriority() {
		return 105;
	}
}
