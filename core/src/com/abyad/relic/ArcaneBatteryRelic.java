package com.abyad.relic;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.magic.AbstractMagic;
import com.abyad.sprite.AbstractSpriteSheet;

public class ArcaneBatteryRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(ArcaneBatteryRelic.class);
	}
	
	public ArcaneBatteryRelic() {
		super("ARCANE BATTERY", "Sometimes restore Mana when damaging enemies with magic", 0.08f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("ARCANE_BATTERY"));
	}

	@Override
	public float getActivationRate() {
		return super.getActivationRate() * (0.5f * (getCount() + 1));
	}
	
	@Override
	public void onMagicHit(PlayerCharacter player, AbstractMagic magicType, HitEvent attack, AbstractEntity hit) {
		if (attack.getDamage() > 0) {
			player.addPartialMana(1);
		}
	}
	
	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
