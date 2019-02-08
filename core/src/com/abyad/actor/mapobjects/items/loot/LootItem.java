package com.abyad.actor.mapobjects.items.loot;

import com.abyad.actor.mapobjects.items.MapItem;
import com.abyad.interfaces.Interactable;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class LootItem extends MapItem implements Interactable{
	
	protected boolean isInteractable;
	
	public LootItem(float x, float y, Vector2 velocity, TextureRegion tex) {
		super(tex, 0);
		setX(x);
		setY(y);
		this.velocity = velocity;
	}
	
	public void spawn() {
		interactables.add(this);
		updateCollideAndInteractBox();
	}
	
	@Override
	public void act(float delta) {
		if (velocity.len2() > 0) {
			move(velocity.x, velocity.y);
			velocity.setLength(0.95f * velocity.len());
			if (velocity.len() < 0.2f) velocity.setLength(0);
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (isInteractable) {
			setScale(1.35f);
		}
		else {
			setScale(1.0f);
		}
		
		super.draw(batch, a);
		
		isInteractable = false;
	}
	
	@Override
	public void setCanInteract(boolean flag) {
		isInteractable = flag;
	}
	
	@Override
	public boolean remove() {
		interactables.remove(this);
		return super.remove();
	}
}
