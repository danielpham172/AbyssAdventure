package com.abyad.actor.cosmetic;

import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CosmeticParticle extends Actor{

	private TextureRegion texture;
	private float height;
	
	private Vector2 velocity;
	private float heightVelocity;
	
	private int lifetime;
	
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
		texture = AbstractSpriteSheet.spriteSheets.get("PARTICLES").getSprite(color);
		setX(x);
		setY(y);
		setHeight(height);
		setOriginX(texture.getRegionWidth() / 2);
		setOriginY(texture.getRegionHeight() / 2);
		
		this.velocity = velocity;
		this.heightVelocity = heightVelocity;
		this.lifetime = lifetime;
	}
	
	@Override
	public void act(float delta) {
		setX(getX() + velocity.x);
		setY(getY() + velocity.y);
		setHeight(getHeight() + heightVelocity);
		
		lifetime--;
		if (lifetime <= 0) remove();
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			batch.draw(texture, getX() - getOriginX(), getY() - getOriginY()+ height, getOriginX(),
				getOriginY(), texture.getRegionWidth(), texture.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
		}
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
}
