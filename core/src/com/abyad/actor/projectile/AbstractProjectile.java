package com.abyad.actor.projectile;

import java.util.ArrayList;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.sprite.ProjectileSprite;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class AbstractProjectile extends Actor{

	protected AbstractEntity source;
	
	protected Vector2 velocity;
	
	protected ProjectileSprite sprite;
	protected String state;				//String used to figure out what the projectile is doing
	protected int framesSinceLast;		//The amount of frames that have passed since it changed state (used for animation)
	protected float frameFraction;		//The amount of partial frames that have passed
	
	protected float height;
	
	protected boolean markForRemoval;
	
	public AbstractProjectile(float x, float y, AbstractEntity source) {
		setX(x);
		setY(y);
		
		this.source = source;
	}
	
	@Override
	public void act(float delta) {
		move(velocity);
		if (markForRemoval) remove();
	}
	
	/**
	 * Standard move method. Changes the entity's x and y and updates it hitbox
	 * @param x		The x change
	 * @param y		The y change
	 */
	public void move(float x, float y) {
		setX(getX() + x);
		setY(getY() + y);
		updateHitbox();
	}
	
	/**
	 * Same as above, but using a Vector2 instead
	 * @param velocity		The velocity to change it by
	 */
	public void move(Vector2 velocity) {
		move(velocity.x, velocity.y);
	}
	
	/**
	 * Sets the state of the entity. If the state is the same, increments the frameSinceLast by 1.
	 * @param newState		The state to change into
	 */
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
	/**
	 * Same as above, but allows for a change amount. Can be a decimal/fraction (which adds to frame fraction)
	 * @param newState		The state to change into
	 * @param change		How much change to add
	 */
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
	
	/**
	 * A return method for getting the velocity of the entity.
	 * @return		The entity's velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	/**
	 * The draw method for the Actor
	 */
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			//Get what sprite(s) to draw given to state, direction, and frame count
			ArrayList<TextureRegion> currentSprites = sprite.getNextFrame(state, velocity, framesSinceLast);
			for (TextureRegion sprite : currentSprites) {
				int centerX = sprite.getRegionWidth() / 2;
				int centerY = sprite.getRegionHeight() / 2;
				batch.draw(sprite, super.getX() - centerX, super.getY() - centerY + height, centerX,
					centerX, sprite.getRegionWidth(), sprite.getRegionHeight(), super.getScaleX(), super.getScaleY(), getRotation());
			}
		}
	}
	
	/**
	 * Checks if the entity is inview of the screen
	 * @return
	 */
	public boolean inView() {
		Rectangle viewbox = getViewbox();
		Vector2 center = new Vector2();
		center = viewbox.getCenter(center);
		return getStage().getCamera().frustum.boundsInFrustum(center.x, center.y, 0,
				getViewbox().width / 2, getViewbox().height / 2, 0);
	}
	
	public abstract void updateHitbox();
	public abstract ArrayList<Rectangle> getHitbox();
	public abstract ArrayList<Rectangle> getHitbox(float x, float y);
	public abstract Rectangle getViewbox();
	
	/**
	 * Collision method for checking overlapping rectangles in list. Could probably be used as a Utility function
	 * @param first		First list of rectangles
	 * @param second	Second list of rectangles
	 * @return		Whether at least one rectangle overlaps
	 */
	protected boolean isOverlapping(ArrayList<Rectangle> first, ArrayList<Rectangle> second) {
		for (Rectangle f : first) {
			for (Rectangle s : second) {
				if (f.overlaps(s)) return true;
			}
		}
		return false;
	}
}
