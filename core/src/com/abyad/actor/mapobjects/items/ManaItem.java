package com.abyad.actor.mapobjects.items;

import com.abyad.actor.cosmetic.CosmeticParticle;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Vector2;

public class ManaItem extends AutoItem{

	private int framesPassed;
	
	public ManaItem() {
		super(AbstractSpriteSheet.spriteSheets.get("PICKUPS").getSprite("MANA"), 60, false);
	}
	
	public ManaItem(float x, float y, Vector2 velocity) {
		this();
		this.velocity = velocity;
		setX(x);
		setY(y);
	}

	@Override
	public void act(float delta) {
		if (framesPassed >= 10 || Math.random() < 0.1) {
			Vector2 pointing = new Vector2(0, 0.5f * (float)((Math.random() * 0.4) + 0.8f));
			pointing.rotate(20 - (float)(Math.random() * 20 * 2));
			int lifetime = (int)(((Math.random() * 0.4) + 0.8) * 30);
			float randomXModifier = (float)(Math.random() * 6f) - 3f;
			float randomYModifier = (float)(Math.random() * 6f) - 3f;
			CosmeticParticle particle = new CosmeticParticle(tex, getX() + randomXModifier, getY() + randomYModifier, 0.0f,
					new Vector2(pointing.x, 0), pointing.y, lifetime);
			particle.setScale(1/8f);
			particle.setRotation((float)(Math.random() * 360f));
			getStage().addActor(particle);
			framesPassed = 0;
		}
		framesPassed++;
		super.act(delta);
	}
	
	@Override
	public void playerPickup(PlayerCharacter player) {
		player.addPartialMana(1);
	}
	
}
