package com.abyad.relic;

import com.abyad.actor.cosmetic.BattleText;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Vector2;

public class GreenCloakRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(GreenCloakRelic.class);
	}
	
	public GreenCloakRelic() {
		super("GREEN CLOAK", "Very rarely evade an attack", 0.02f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("GREEN_CLOAK"));
	}

	@Override
	public float getActivationRate() {
		return Math.min(super.getActivationRate() * (0.5f * (getCount() + 1)), .50f);
	}
	
	@Override
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		if (defense.getDamage() > 0 || defense.getKnockbackLength() > 0) {
			defense.setDamage(0);
			defense.setKnockbackLength(0);
			Vector2 upVelocity = new Vector2(0, 1.25f);
			BattleText text = new BattleText("EVADE!", player.getCenterX(), player.getCenterY(), upVelocity.cpy(), 0.98f, 30, true);
			text.setScale(0.2f);
			player.getStage().addActor(text);
		}
	}
	
	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
