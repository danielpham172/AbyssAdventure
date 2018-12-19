package com.abyad.actor.entity;

import java.util.ArrayList;

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
	
	private Vector2 knockbackVelocity = new Vector2();
	private int knockbackLength = 0;
	
	private String task = "WANDER";
	private int timeSinceTask = 0;
	
	private Vector2 wanderDirection = new Vector2(1, 1);
	
	public ZombieCharacter() {
		super();
		
		sprite = new EntitySprite(Assets.manager.get(Assets.zombie));

		wanderDirection.setLength(1);
		wanderDirection.rotate((float)(Math.random() * 360));
		updateHitbox();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (knockbackLength <= 0) {
			PlayerCharacter player = getNearestPlayerInRange(80);
			if (player != null) {
				setTask("CHASE");
			}
			else {
				setTask("WANDER");
			}
			
			if (task.equals("CHASE")) {
				getVelocity().set(player.getCenterX() - getCenterX(), player.getCenterY() - getCenterY());
				getVelocity().setLength(0.3f);
				wanderDirection.setAngle(getVelocity().angle());
				move(getVelocity());
				setState("MOVE", 0.3f/2.0f);
				if (framesSinceLast == 0) frameFraction = (float)(Math.random() * 240);
				checkCollisions();
			}
			else if (task.equals("WANDER")){
				getVelocity().set(wanderDirection.x, wanderDirection.y).setLength(0.3f);
				setState("MOVE", 0.3f/2.0f);
				if (Math.random() * timeSinceTask / 120.0 > 1.0) {
					wanderDirection.rotate((float)(Math.random() * 60) - 30f);
					timeSinceTask = 0;
				}
				checkCollisions();
				move(getVelocity());
				if (hitWall) {
					wanderDirection.rotate(180 + (float)(Math.random() * 180) - 90f);
					timeSinceTask = 0;
				}
			}
		}
		else {
			move(knockbackVelocity);
			knockbackLength--;
		}
	}
	
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
	
	public void checkCollisions() {
		ArrayList<AbstractEntity> entities = getEntities();
		for (AbstractEntity entity : entities) {
			ArrayList<Rectangle> otherHitbox = entity.getHitbox();
			if (!isSameTeam(entity) && isColliding(otherHitbox)) {
				Vector2 knockback = new Vector2(entity.getCenterX() - getCenterX(), entity.getCenterY() - getCenterY());
				knockback.setLength(4.0f);
				entity.takeDamage(this, 0, knockback, 8);
			}
			else if (isSameTeam(entity) && isColliding(otherHitbox)) {
				Vector2 smallCollision = new Vector2(entity.getCenterX() - getCenterX(), entity.getCenterY() - getCenterY());
				smallCollision.setLength(Math.min(0.2f / smallCollision.len(), 0.1f));
				entity.move(smallCollision);
				move(smallCollision.rotate(180));
			}
		}
	}
	
	public boolean isColliding(ArrayList<Rectangle> others) {
		for (Rectangle other : others) {
			for (Rectangle hitbox : hitboxes) {
				if (other.overlaps(hitbox)) return true;
			}
		}
		return false;
	}
	
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
	
	@Override
	public void takeDamage(Actor source, int damage, Vector2 knockback, int kbLength) {
		if (!isInvuln()) {
			knockbackVelocity = knockback.cpy();
			knockbackLength = kbLength;
			invulnLength = 40;
		}
	}

}
