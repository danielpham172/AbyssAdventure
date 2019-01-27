package com.abyad.relic;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;

public class VampiricFangRelic extends Relic{

	public VampiricFangRelic() {
		super("VAMPIRIC FANG", "Chance to heal when attacking", 0.04f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("VAMPIRIC_FANG"));
	}

	@Override
	public void onHit(PlayerCharacter player, HitEvent attack, AbstractEntity hit) {
		player.restoreHealth(1);
	}
}
