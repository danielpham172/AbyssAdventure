package com.abyad.actor.cosmetic;

import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CosmeticParticle extends Actor{

	private TextureRegion texture;
	private float height;
	
	private Vector2 velocity;
	private float heightVelocity;
	
	private int lifetime;
	private float transparency = 1.0f;
	
	private boolean lockedToActor;
	private Actor follow;
	
	public CosmeticParticle(TextureRegion tex, float x, float y, float height, Vector2 velocity, float heightVelocity, int lifetime) {
		texture = tex;
		setX(x);
		setY(y);
		setHeight(height);
		setOriginX(texture.getRegionWidth() / 2);
		setOriginY(texture.getRegionHeight() / 2);
		
		this.velocity = velocity;
		this.heightVelocity = heightVelocity;
		this.lifetime = lifetime;
	}
	
	public CosmeticParticle(String color, float x, float y, float height, Vector2 velocity, float heightVelocity, int lifetime) {
		this(AbstractSpriteSheet.spriteSheets.get("PARTICLES").getSprite(color), x, y, height, velocity, heightVelocity, lifetime);
	}
	
	public CosmeticParticle(TextureRegion tex, float x, float y, float height, Vector2 velocity, float heightVelocity, int lifetime, Actor follow) {
		this(tex, x, y, height, velocity, heightVelocity, lifetime);
		this.follow = follow;
		lockedToActor = true;
		float currentXDiff = x - follow.getX();
		float currentYDiff = y - follow.getY();
		setX(currentXDiff);
		setY(currentYDiff);
	}
	
	public CosmeticParticle(TextureRegion tex, float x, float y, float height, Vector2 velocity, float heightVelocity, int lifetime, float transparency) {
		this(tex, x, y, height, velocity, heightVelocity, lifetime);
		this.transparency = transparency;
	}
	
	@Override
	public void act(float delta) {
		moveBy(velocity.x, velocity.y);
		setHeight(getHeight() + heightVelocity);
		
		lifetime--;
		if (lifetime <= 0) remove();
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			batch.setColor(1.0f, 1.0f, 1.0f, transparency);
			batch.draw(texture, getX() - getOriginX(), getY() - getOriginY() + height, getOriginX(),
					getOriginY(), texture.getRegionWidth(), texture.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
			batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}
	
	@Override
	public float getX() {
		if (lockedToActor) return super.getX() + follow.getX();
		return super.getX();
	}
	
	@Override
	public float getY() {
		if (lockedToActor) return super.getY() + follow.getY();
		return super.getY();
	}
	
	public boolean inView() {
		return getStage().getCamera().frustum.boundsInFrustum(getX(), getY(), 0,
				texture.getRegionWidth() / 2, texture.getRegionHeight() / 2, 0);
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}
	
	public static void spawnParticle(TextureRegion tex, float x, float y, float height, float xRange, float yRange, float heightRange,
			float particleSpeed, float maxSpeedChangePercent, float offsetAngle, float angleRange, float transparency,
			float scale, float particleLifetime, float maxLifetimeChangePercent, int count, Stage stage) {
		for (int c = 0; c < count; c++) {
			Vector2 pointing = new Vector2(0, particleSpeed * (float)((Math.random() * maxSpeedChangePercent * 2) + (1.0f - maxSpeedChangePercent)));
			pointing.rotate(angleRange - (float)(Math.random() * angleRange * 2) + offsetAngle);
			int lifetime = (int)((Math.random() * maxLifetimeChangePercent * 2) + (1.0 - maxLifetimeChangePercent) * particleLifetime);
			float randomXModifier = (float)(Math.random() * xRange * 2) - xRange;
			float randomYModifier = (float)(Math.random() * yRange * 2) - yRange;
			float randomHeightModifier = (float)(Math.random() * heightRange * 2) - heightRange;
			CosmeticParticle particle = new CosmeticParticle(tex, x + randomXModifier, y + randomYModifier, height + randomHeightModifier,
					new Vector2(pointing.x, 0), pointing.y, lifetime);
			particle.setTransparency(transparency);
			particle.setScale(scale);
			
			stage.addActor(particle);
		}
	}
	
	public static void spawnParticle(TextureRegion tex, float x, float y, float height, float xRange, float yRange, float heightRange,
			float particleSpeed, float maxSpeedChangePercent, float offsetAngle, float angleRange, float transparency,
			float scale, float particleLifetime, float maxLifetimeChangePercent, int count, Actor follow) {
		for (int c = 0; c < count; c++) {
			Vector2 pointing = new Vector2(0, particleSpeed * (float)((Math.random() * maxSpeedChangePercent * 2) + (1.0f - maxSpeedChangePercent)));
			pointing.rotate(angleRange - (float)(Math.random() * angleRange * 2) + offsetAngle);
			int lifetime = (int)((Math.random() * maxLifetimeChangePercent * 2) + (1.0 - maxLifetimeChangePercent) * particleLifetime);
			float randomXModifier = (float)(Math.random() * xRange * 2) - xRange;
			float randomYModifier = (float)(Math.random() * yRange * 2) - yRange;
			float randomHeightModifier = (float)(Math.random() * heightRange * 2) - heightRange;
			CosmeticParticle particle = new CosmeticParticle(tex, x + randomXModifier, y + randomYModifier, height + randomHeightModifier,
					new Vector2(pointing.x, 0), pointing.y, lifetime, follow);
			particle.setTransparency(transparency);
			particle.setScale(scale);
			
			follow.getStage().addActor(particle);
		}
	}
}
