package com.abyad.relic;

import com.abyad.actor.cosmetic.BattleText;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Vector2;

public class LifeRingRelic extends Relic{

	private int charges;
	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(LifeRingRelic.class);
	}
	
	public LifeRingRelic() {
		super("LIFE RING", "Saves yourself from a fatal hit", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("LIFE_RING"));
	}

	@Override
	public void onPickup(PlayerCharacter player) {
		charges += 1;
	}
	
	@Override
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		if (player.getHP() <= defense.getDamage() && charges > 0) {
			defense.setDamage(0);
			Vector2 upVelocity = new Vector2(0, 1.2f);
			BattleText text = new BattleText("SAVED!", player.getCenterX(), player.getCenterY(), upVelocity.cpy(), 0.97f, 30, true);
			text.setScale(0.25f);
			player.getStage().addActor(text);
			charges--;
		}
	}
	
	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
