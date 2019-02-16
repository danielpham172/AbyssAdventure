package com.abyad.actor.mapobjects.items.carrying;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.items.loot.LootItem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class CarryingItem extends LootItem{

	private float weight;
	
	private boolean directionalItem;
	
	private TextureRegion front;
	private TextureRegion back;
	private TextureRegion left;
	private TextureRegion right;
	
	public CarryingItem(float x, float y, Vector2 velocity, TextureRegion tex, float weight) {
		super(x, y, velocity, tex);
		this.weight = weight;
	}
	
	public CarryingItem(float x, float y, Vector2 velocity, TextureRegion front, TextureRegion right, TextureRegion left, TextureRegion back, float weight) {
		super(x, y, velocity, front);
		this.front = front;
		this.right = right;
		this.left = left;
		this.back = back;
		directionalItem = true;
		updateTexture(velocity);
		this.weight = weight;
	}
	
	public boolean drawDirectionally() {
		return directionalItem;
	}
	
	public void updateTexture(Vector2 direction) {
		if (drawDirectionally()) {
			int d = (int)((direction.angle() + 45f) / 90f) % 4;
			if (d == 0) tex = right;
			if (d == 1) tex = back;
			if (d == 2) tex = left;
			if (d == 3) tex = front;
		}
	}
	
	public Vector2 getOffsetDraw(Vector2 direction) {
		return new Vector2(0, (getOriginY() * 1.25f));
	}
	
	@Override
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
