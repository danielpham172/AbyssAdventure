package com.abyad.actor.mapobjects.items;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Vector2;

public class HeartItem extends AutoItem{
	
	public HeartItem() {
		super(AbstractSpriteSheet.spriteSheets.get("PICKUPS").getSprite("HEART"), 80, false);
	}
	
	public HeartItem(float x, float y, Vector2 velocity) {
		this();
		this.velocity = velocity;
		setX(x);
		setY(y);
	}

	@Override
	public void playerPickup(PlayerCharacter player) {
		player.restoreHealth(1);
	}

}
