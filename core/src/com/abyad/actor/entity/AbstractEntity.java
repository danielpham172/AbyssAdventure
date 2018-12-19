package com.abyad.actor.entity;

import java.util.ArrayList;

import com.abyad.sprite.EntitySprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class AbstractEntity extends Actor{

	private static ArrayList<AbstractEntity> entities = new ArrayList<AbstractEntity>();
	private int hp;
	private int maxHP;
	
	protected EntitySprite sprite;
	protected Vector2 velocity;
	protected int invulnLength;
	
	protected String state;
	protected int framesSinceLast;
	protected float frameFraction;
	
	public AbstractEntity() {
		entities.add(this);
		
		velocity = new Vector2();
		invulnLength = 0;
		
		state = "IDLE";
		framesSinceLast = 0;
		frameFraction = 0f;
	}
	
	@Override
	public void act(float delta) {
		if (invulnLength > 0) {
			invulnLength--;
		}
	}
	
	public void move(float x, float y) {
		setX(getX() + x);
		setY(getY() + y);
		updateHitbox();
	}
	
	public void move(Vector2 velocity) {
		move(velocity.x, velocity.y);
	}
	
	
	public abstract float getCenterX();
	public abstract float getCenterY();
	public Vector2 getCenter() {
		return new Vector2(getCenterX(), getCenterY());
	}
	
	public void setState(String newState) {
		if (state.equals(newState)) {
			framesSinceLast++;
		}
		else {
			state = newState;
			framesSinceLast = 0;
			frameFraction = 0;
		}
	}
	public void setState(String newState, float change) {
		if (state.equals(newState)) {
			frameFraction += change;
			while (frameFraction >= 1.0f) {
				framesSinceLast++;
				frameFraction--;
			}
		}
		else {
			state = newState;
			framesSinceLast = 0;
			frameFraction = 0;
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			if ((getInvulnLength() / 4) % 2 == 0) {
				ArrayList<TextureRegion> currentSprites = sprite.getNextFrame(state, velocity, framesSinceLast);
				for (TextureRegion sprite : currentSprites) {
					int centerX = sprite.getRegionWidth() / 2;
					int centerY = sprite.getRegionHeight() / 2;
					batch.draw(sprite, super.getX() - centerX, super.getY() - centerY, centerX,
						centerX, sprite.getRegionWidth(), sprite.getRegionHeight(), super.getScaleX(), super.getScaleY(), getRotation());
				}
			}
		}
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public abstract String getTeam();
	public boolean isSameTeam(AbstractEntity other) {
		return getTeam().equals(other.getTeam());
	}
	public abstract void takeDamage(Actor source, int damage, Vector2 knockback, int kbLength);
	public boolean isInvuln() {
		return invulnLength > 0;
	}
	public int getInvulnLength() {
		return invulnLength;
	}
	
	public abstract void updateHitbox();
	public abstract ArrayList<Rectangle> getHitbox();
	public abstract ArrayList<Rectangle> getHitbox(float x, float y);
	public abstract ArrayList<Rectangle> getCollideBox();
	public abstract ArrayList<Rectangle> getCollideBox(float x, float y);
	public abstract Rectangle getViewbox();
	
	public boolean inView() {
		Rectangle viewbox = getViewbox();
		Vector2 center = new Vector2();
		center = viewbox.getCenter(center);
		return getStage().getCamera().frustum.boundsInFrustum(center.x, center.y, 0,
				getViewbox().width / 2, getViewbox().height / 2, 0);
	}
	
	@Override
	public boolean remove() {
		entities.remove(this);
		return super.remove();
	}
	
	public static ArrayList<AbstractEntity> getEntities(){
		return entities;
	}
}
