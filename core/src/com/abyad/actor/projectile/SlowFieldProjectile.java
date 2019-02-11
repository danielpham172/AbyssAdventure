package com.abyad.actor.projectile;

import java.util.ArrayList;

import com.abyad.actor.cosmetic.CosmeticParticle;
import com.abyad.actor.entity.AbstractEntity;
import com.abyad.data.StatusEffectData;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.ProjectileSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SlowFieldProjectile extends OnGroundProjectile{

	private ArrayList<Rectangle> hitboxes;
	private ArrayList<Rectangle> tempHitboxes;
	
	private ArrayList<AbstractEntity> healed;
	
	private static Rectangle baseBox = new Rectangle(0, 0, 64, 64);
	private static TextureRegion particleTexture = AbstractSpriteSheet.spriteSheets.get("STATUS_ARROW").getSprite("DOWN");
	
	private static final int LIFETIME = 600;
	
	public SlowFieldProjectile(float x, float y, AbstractEntity source) {
		super(x, y, source);
		
		hitboxes = new ArrayList<Rectangle>();
		tempHitboxes = new ArrayList<Rectangle>();
		healed = new ArrayList<AbstractEntity>();
		
		Rectangle hitbox = new Rectangle(baseBox);
		Rectangle tempHitbox = new Rectangle(baseBox);
		
		hitboxes.add(hitbox);
		tempHitboxes.add(tempHitbox);
		
		sprite = (ProjectileSprite)AbstractSpriteSheet.spriteSheets.get("SLOW_FIELD_PROJECTILE");
		velocity = new Vector2(0, 0);
		setScale(2.0f);
	}
	
	@Override
	public void act(float delta) {
		
		ArrayList<Rectangle> hurtboxes = getHitbox();
		ArrayList<AbstractEntity> entities = AbstractEntity.getEntities();
		for (AbstractEntity entity : entities) {
			if (!source.isSameTeam(entity)) {
				ArrayList<Rectangle> otherHitbox = entity.getHitbox();
				if (isOverlapping(hurtboxes, otherHitbox)) {
					StatusEffectData slow = new StatusEffectData("SLOW", 0.005f);
					entity.applyStatusEffect(slow);
				}
			}
		}
		
//		PlayStage stage = (PlayStage)getStage();
//		ArrayList<Rectangle> walls = stage.getSurroundingWallBoxes(getX(), getY(), 1);		//Gets surrounding wall tiles as rectangle collisions
//		ArrayList<Rectangle> collisionBox = new ArrayList<Rectangle>();
//		collisionBox.add(new Rectangle(getX() - 4, getY() - 4, 8, 8));
//		if (isOverlapping(walls, collisionBox)) {
//			markForRemoval = true;
//		}
		
		framesSinceLast++;
		if (framesSinceLast % 2 == 0) {
			slowParticleEffects(getX(), getY(), 1);
		}
		
		if (framesSinceLast >= LIFETIME) {
			markForRemoval = true;
		}
		
		super.act(delta);
	}
	
	public void slowParticleEffects(float x, float y, int count) {
		CosmeticParticle.spawnParticle(particleTexture, x, y, 10f, 32f, 32f, 2f,
				0.2f, 0.1f, 180.0f, 0, 0.5f, 0.5f, 60, 0.2f, count, getStage());
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
