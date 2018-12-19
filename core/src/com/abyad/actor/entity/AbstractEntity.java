package com.abyad.actor.entity;

import java.util.ArrayList;

import com.abyad.sprite.EntitySprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class AbstractEntity extends Actor{

	private static ArrayList<AbstractEntity> entities = new ArrayList<AbstractEntity>();	//All entities in a list that are created
	private int hp;						//HP variable
	private int maxHP;					//Max HP variable

	protected EntitySprite sprite;		//The sprite of the entity, used to get the correct sprite at right times
	protected Vector2 velocity;			//The velocity (speed and direction) the entity is going in
	protected int invulnLength;			//The current length of their invuln period. Blinks in animation
	
	protected String state;				//String used to figure out what the entity is doing (ie. Walking or Idle)
	protected int framesSinceLast;		//The amount of frames that have passed since it changed state (used for animation)
	protected float frameFraction;		//The amount of partial frames that have passed, mainly used for walking slowly and such
	
	/**
	 * Initiates an AbstractEntity. Most important is that it adds it to the array list
	 */
	public AbstractEntity() {
		entities.add(this);
		
		velocity = new Vector2();
		invulnLength = 0;
		
		state = "IDLE";
		framesSinceLast = 0;
		frameFraction = 0f;
	}
	
	/**
	 * What the entity does per frame. For the base, only decrementing the invuln length is of use
	 */
	@Override
	public void act(float delta) {
		if (invulnLength > 0) {
			invulnLength--;
		}
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
	 * Get center methods. Mainly used for getting the center of the entity's hitbox.
	 */
	public abstract float getCenterX();
	public abstract float getCenterY();
	public Vector2 getCenter() {
		return new Vector2(getCenterX(), getCenterY());
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
	 * The draw method for the Actor
	 */
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			if ((getInvulnLength() / 4) % 2 == 0) {		//Line to get the blinking animation of being invuln
				//Get what sprite(s) to draw given to state, direction, and frame count
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
	
	/**
	 * A return method for getting the velocity of the entity.
	 * @return		The entity's velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}
	
	/**
	 * getTeam returns a String on what team the entity is on, such as "PLAYER" or "MONSTER". The isSameTeam method is a convenience method
	 * to check equality.
	 */
	public abstract String getTeam();
	public boolean isSameTeam(AbstractEntity other) {
		return getTeam().equals(other.getTeam());
	}
	
	/**
	 * Take damage method for all entities.
	 * @param source			From where the entity took damage from
	 * @param damage			The amount of damage to take
	 * @param knockback			The knockback force and direction
	 * @param kbLength			How long the knockback should last
	 */
	public abstract void takeDamage(Actor source, int damage, Vector2 knockback, int kbLength);
	
	/**
	 * The following are methods for getting invuln conditions
	 */
	public boolean isInvuln() {
		return invulnLength > 0;
	}
	public int getInvulnLength() {
		return invulnLength;
	}
	
	/**
	 * Hitbox is the box used for collision on attacks (where the entity can get hurt). Collidebox is used for moving around to
	 * not hit walls and other obstacles (could be the same as hitbox). Viewbox is used to see if the entity is in view on the
	 * screen for drawing optimization.
	 */
	public abstract void updateHitbox();
	public abstract ArrayList<Rectangle> getHitbox();
	public abstract ArrayList<Rectangle> getHitbox(float x, float y);
	public abstract ArrayList<Rectangle> getCollideBox();
	public abstract ArrayList<Rectangle> getCollideBox(float x, float y);
	public abstract Rectangle getViewbox();
	
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
	
	@Override
	public boolean remove() {
		entities.remove(this);
		return super.remove();
	}
	
	public static ArrayList<AbstractEntity> getEntities(){
		return entities;
	}
}
