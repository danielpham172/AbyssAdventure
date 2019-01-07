package com.abyad.actor.mapobjects.items;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Vector2;

public class GoldItem extends AutoItem{

	public GoldItem(float x, float y, Vector2 velocity) {
		super(AbstractSpriteSheet.spriteSheets.get("PICKUPS").getSprite("GOLD"), 50, false);
		this.velocity = velocity;
		setX(x);
		setY(y);
	}

	@Override
	public void playerPickup(PlayerCharacter player) {
		player.modifyGold(1);
	}
	
}
