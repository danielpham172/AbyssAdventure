package com.abyad.relic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;

public class ManaShieldRelic extends Relic{

	public ManaShieldRelic() {
		super("MANA SHIELD", "Chance to take one damage as mana when hit", 0.1f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("MANA_SHIELD"));
	}

	@Override
	public float getActivationRate() {
		return Math.min(super.getActivationRate() * (0.5f * (getCount() + 1)), .95f);
	}
	
	@Override
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		if (player.getMana() > 0 && defense.getDamage() > 0) {
			player.removeMana(1);
			defense.setDamage(defense.getDamage() - 1);
		}
	}
	
	@Override
	public int getPriority() {
		return 102;
	}
}
