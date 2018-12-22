package com.abyad.actor.entity;

import java.util.ArrayList;

import com.abyad.actor.cosmetic.DeathAnimation;
import com.abyad.actor.mapobjects.items.HeartItem;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.EntitySprite;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ZombieCharacter extends HumanoidEntity{
	
	private Vector2 knockbackVelocity = new Vector2();		//Knockback velocity
	private int knockbackLength = 0;						//How long the zombie is getting knocked back

	private String task = "WANDER";							//The current thing the zombie is doing (like CHASE or WANDER)
	private int timeSinceTask = 0;							//How long they have doing this task
	
	private Vector2 wanderDirection = new Vector2(1, 0);	//The direction the zombie is wandering in
	
	private final float SPEED = 0.3f;
	
	public ZombieCharacter() {
		super();
		
		sprite = (EntitySprite)AbstractSpriteSheet.spriteSheets.get("ZOMBIE");	//Gotta set the zombie sprite

		wanderDirection.rotate((float)(Math.random() * 360));		//Randomly set a direction for the zombie to wander
		updateHitbox();
	}
	
	/**
	 * This is where all the AI of zombie is
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (!markForRemoval) {
			if (knockbackLength <= 0) {
				//If zombie is not getting knocked back
				PlayerCharacter player = getNearestPlayerInRange(80);		//Find the nearest player in range if any
				if (player != null) {
					setTask("CHASE");		//Set the task to chase player has been found
				}
				else {
					setTask("WANDER");		//Wander if no player was found
				}
				
				if (task.equals("CHASE")) {
					//If chasing, have the velocity point towards the player then set the speed by using the length
					getVelocity().set(player.getCenterX() - getCenterX(), player.getCenterY() - getCenterY());
					getVelocity().setLength(SPEED);
					//Also set the wander direction to be the same so they don't just randomly splay off
					wanderDirection.setAngle(getVelocity().angle());
					move(getVelocity());
					setState("MOVE", SPEED/2.0f);		//Sets the state for animation. The fraction is the slow down the animation to match the speed
					if (framesSinceLast == 0) frameFraction = (float)(Math.random() * 240);		//Modifies the starting cycle so not all zombies do the same walk
					checkCollisions();
				}
				else if (task.equals("WANDER")){
					//If wandering, just move in the same direction as the wander with a length change
					getVelocity().set(wanderDirection.x, wanderDirection.y).setLength(0.3f);
					setState("MOVE", SPEED/2.0f);
					if (framesSinceLast == 0) frameFraction = (float)(Math.random() * 240);		//Modifies the starting cycle so not all zombies do the same walk
					if (Math.random() * timeSinceTask / 120.0 > 1.0) {
						//Randomly change the direction slightly after a certain amount of time
						wanderDirection.rotate((float)(Math.random() * 60) - 30f);
						timeSinceTask = 0;
					}
					checkCollisions();
					move(getVelocity());
					if (hitWall) {
						//If a wall was hit during collision, turn around
						wanderDirection.rotate(180 + (float)(Math.random() * 180) - 90f);
						timeSinceTask = 0;
					}
				}
			}
			else {
				//Move the zombie by knockback
				move(knockbackVelocity);
				knockbackLength--;
			}
		}
	}
	
	/**
	 * Method used to find the nearest player in range.
	 * @param range			The maximum range to player can be in
	 * @return		null if no players in range, or the nearest player
	 */
	public PlayerCharacter getNearestPlayerInRange(float range){
		ArrayList<PlayerCharacter> players = PlayerCharacter.getPlayers();
		PlayerCharacter nearest = null;
		float closest = (float)Math.pow((double)range, 2);
		for (PlayerCharacter player : players) {
			float distance = (float)Math.pow(player.getCenterX() - getCenterX(), 2) +
					(float)Math.pow(player.getCenterY() - getCenterY(), 2);
			if (distance <= closest) {
				nearest = player;
				closest = distance;
			}
		}
		return nearest;
	}
	
	/**
	 * Sets the task of the zombie. Increments timeSinceTask if it was already doing that task
	 * @param newTask
	 */
	public void setTask(String newTask) {
		if (task.equals(newTask)) {
			timeSinceTask++;
		}
		else {
			task = newTask;
			timeSinceTask = 0;
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		//drawHitbox(batch, a);
		super.draw(batch, a);
	}
	
	/**
	 * Collision checking for bumping into players (for damage) and other entities (for shifting)
	 */
	public void checkCollisions() {
		ArrayList<AbstractEntity> entities = getEntities();
		for (AbstractEntity entity : entities) {
			ArrayList<Rectangle> otherHitbox = entity.getHitbox();
			if (!isSameTeam(entity) && isOverlapping(hitboxes, otherHitbox)) {
				//If the other entity was a player (or non-ally to the zombie) and is in collision, deal damage to it
				Vector2 knockback = new Vector2(entity.getCenterX() - getCenterX(), entity.getCenterY() - getCenterY());
				knockback.setLength(4.0f);
				int damage = 1;
				int knockbackLength = 8;
				entity.takeDamage(new HitEvent(this, entity, damage, knockback, knockbackLength));
			}
			else if (isSameTeam(entity) && isOverlapping(hitboxes, otherHitbox)) {
				//If it is on the same team and colliding, do a small collision to shift it out of each other
				//This is mostly for anti-clumping together
				Vector2 smallCollision = new Vector2(entity.getCenterX() - getCenterX(), entity.getCenterY() - getCenterY());
				smallCollision.setLength(Math.min(0.2f / smallCollision.len(), 0.1f));
				entity.move(smallCollision);
				move(smallCollision.rotate(180));
			}
		}
	}
	
	/**
	 * Unoptimized drawing of hitboxes for debugging. Do not use unless you want to have a lot of trouble with closing the application
	 * @param batch
	 * @param a
	 */
	public void drawHitbox(Batch batch, float a) {
		for (Rectangle hitbox : hitboxes) {
			Pixmap pixmap = new Pixmap((int)hitbox.getWidth(), (int)hitbox.getHeight(), Format.RGBA8888 );
			pixmap.setColor( 1, 0, 0, 0.25f );
			pixmap.fill();
			Texture box = new Texture( pixmap );
			pixmap.dispose();
			
			batch.draw(box, hitbox.getX(), hitbox.getY());
		}
	}
	
	@Override
	public String getTeam() {
		return "MONSTERS";
	}
	
	/**
	 * Overriden method so that invuln and knockback is applied.
	 */
	@Override
	public void takeDamage(HitEvent event) {
		if (!isInvuln()) {
			knockbackVelocity = event.getKnockbackVelocity();
			knockbackLength = event.getKnockbackLength();
			hp -= event.getDamage();
			if (isDead()) {
				DeathAnimation deathAnimation = new DeathAnimation(getCenterX(), getCenterY());
				getStage().addActor(deathAnimation);
				dropHeart();
				markForRemoval = true;
			}
			else {
				invulnLength = 40;
			}
		}
	}
	
	public void dropHeart() {
		Vector2 velocity = new Vector2(1, 1);
		velocity.setAngle((float)(Math.random() * 360)).setLength((float)(Math.random() * 0.5f) + 1.0f);
		HeartItem heart = new HeartItem(velocity, getCenterX(), getCenterY());
		getStage().addActor(heart);
	}

}
