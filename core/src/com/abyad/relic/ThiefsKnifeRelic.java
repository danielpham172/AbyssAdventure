package com.abyad.relic;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.items.pickups.GoldItem;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Vector2;

public class ThiefsKnifeRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(ThiefsKnifeRelic.class);
	}
	
	
	public ThiefsKnifeRelic() {
		super("THIEF'S KNIFE", "Occasionally gain additional gold when killing enemies", 1.0f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("THIEF'S_KNIFE"));
	}

	@Override
	public void onKill(PlayerCharacter player, AbstractEntity hit) {
		int tries = 1 + getCount();
		for (int i = 0; i < tries; i++) {
			if (Math.random() < 0.06) {
				Vector2 velocity = new Vector2(1, 0);
				velocity.setAngle((float)(Math.random() * 360)).setLength((float)(Math.random() * 0.5f) + 0.5f);
				GoldItem gold = new GoldItem(hit.getCenterX(), hit.getCenterY(), velocity);
				player.getStage().addActor(gold);
			}
		}
	}
	
	@Override
	public int getPriority() {
		return priorityNumber;
	}
}
