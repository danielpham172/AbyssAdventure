package com.abyad.actor.mapobjects.items;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class CarryingItem extends LootItem{

	private float weight;
	
	public CarryingItem(float x, float y, Vector2 velocity, TextureRegion tex, float weight) {
		super(x, y, velocity, tex);
		this.weight = weight;
	}
	
	public boolean interact(PlayerCharacter player) {
		if (player.carryItem(this)) {
			remove();
			return true;
		}
		return false;
	}
	
	public float getWeight() {
		return weight;
	}

}
