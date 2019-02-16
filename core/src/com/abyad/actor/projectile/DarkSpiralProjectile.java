package com.abyad.actor.projectile;

import java.util.ArrayList;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.ProjectileSprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DarkSpiralProjectile extends OnGroundProjectile{

	private ArrayList<Rectangle> hitboxes;
	private ArrayList<Rectangle> tempHitboxes;
	
	private static Rectangle baseBox = new Rectangle(0, 0, 32, 32);
	
	private static final int LIFETIME = 600;
	
	private static final float PULL_RADIUS = 48f;
	private static final float PULL_STRENGTH = 3.0f;
	private static final float PULL_EXPONENT = 2.0f;
	
	public DarkSpiralProjectile(float x, float y, AbstractEntity source) {
		super(x, y, source);
		
		hitboxes = new ArrayList<Rectangle>();
		tempHitboxes = new ArrayList<Rectangle>();
		
		Rectangle hitbox = new Rectangle(baseBox);
		Rectangle tempHitbox = new Rectangle(baseBox);
		
		hitboxes.add(hitbox);
		tempHitboxes.add(tempHitbox);
		
		sprite = (ProjectileSprite)AbstractSpriteSheet.spriteSheets.get("DARK_SPIRAL_PROJECTILE");
		velocity = new Vector2(0, 0);
		setRotation((float)Math.random() * 360f);
	}
	
	@Override
	public void act(float delta) {
		
		updateHitbox();
		ArrayList<AbstractEntity> entities = AbstractEntity.getEntities();
		for (AbstractEntity entity : entities) {
			if (!source.isSameTeam(entity)) {
				Vector2 distance = new Vector2(getX() - entity.getCenterX(), getY() - entity.getCenterY());
				float distanceLength = distance.len();
				if (distanceLength < PULL_RADIUS) {
					if (distanceLength != 0) {
						float pullStrength = PULL_STRENGTH * (float)Math.pow(((PULL_RADIUS - distanceLength) / PULL_RADIUS), PULL_EXPONENT);
						distance.setLength(pullStrength);
						distance.setAngle(distance.angle() - 82f);
						entity.move(distance);
					}
				}
			}
		}
		
		framesSinceLast++;
		
		if (framesSinceLast >= LIFETIME) {
			markForRemoval = true;
		}
		
		setRotation(getRotation() + 5f);
		super.act(delta);
	}

	@Override
	public void updateHitbox() {
		hitboxes.get(0).setCenter(getX(), getY());
	}

	@Override
	public ArrayList<Rectangle> getHitbox() {
		return hitboxes;
	}

	@Override
	public ArrayList<Rectangle> getHitbox(float x, float y) {
		tempHitboxes.get(0).setCenter(x, y);
		return tempHitboxes;
	}

	@Override
	public Rectangle getViewbox() {
		return new Rectangle(getX() - 16, getY() - 16, 32, 32);
	}

}
