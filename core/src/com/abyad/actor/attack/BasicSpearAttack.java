package com.abyad.actor.attack;

import java.util.ArrayList;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.relic.Relic;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BasicSpearAttack extends AttackData{

	private int[] attackLengths = {7, 15, 20, 24};		//The frame thresholds for each sprite. Only the second frame are attack frames
	
	@Override
	public String getName() {
		return "SPEAR";
	}
	@Override
	public void initiateAttack(PlayerCharacter player) {
		if (player.getState().equals("IDLE")) {
			//If the player was in idle, give a small forward movement. May remove or lower this.
			player.getVelocity().setAngle(((int)(player.getVelocity().angle() + 45) / 90) * 90.0f);
			player.getVelocity().setLength(0.1f);
		}
		else if (player.getState().equals("MOVE")) {
			player.getVelocity().scl(1.2f);
		}
	}
	
	@Override
	public void useAttack(PlayerCharacter player, int framesSinceLast) {
		player.getVelocity().setLength(player.getVelocity().len() / 1.10f);
		player.move(player.getVelocity());
		ArrayList<Rectangle> hurtboxes = getHurtboxes(player, framesSinceLast);
		ArrayList<AbstractEntity> entities = AbstractEntity.getEntities();
		for (AbstractEntity entity : entities) {
			if (!player.isSameTeam(entity)) {
				ArrayList<Rectangle> otherHitbox = entity.getHitbox();
				if (isOverlapping(hurtboxes, otherHitbox)) {
					//Attacked someone
					Vector2 knockback = new Vector2(entity.getCenterX() - player.getCenterX(), entity.getCenterY() - player.getCenterY());
					knockback.setLength(3.0f);
					int kbLength = 8;
					int damage = 1;
					HitEvent event = new HitEvent(player, entity, damage, knockback, kbLength);
					player.addPartialMana(1);
					
					//Activate relic hit effects (modifies the event)
					for (Relic relic : player.getRelics()) {
						if (!relic.isOnCooldown() && Math.random() < relic.getActivationRate()) {
							relic.onHit(player, this, event, entity);
						}
					}
					player.applyOnHitStatusEffects(event, entity);
					
					entity.takeDamage(event);
					
					//Activate kill effects if killed entity
					if (entity.isDead()) {
						for (Relic relic : player.getRelics()) {
							if (!relic.isOnCooldown() && Math.random() < relic.getActivationRate()) {
								relic.onKill(player, entity);
							}
						}
					}
				}
			}
		}
	}
	
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
	
	@Override
	public ArrayList<Rectangle> getHurtboxes(PlayerCharacter player, int framesSinceLast) {
		ArrayList<Rectangle> hurtboxes = new ArrayList<Rectangle>();
		int frame = 0;
		int dir = (int)((player.getVelocity().angle() + 45) / 90) % 4; //0 - Right, 1 - Back, 2 - Left, 3 - Front
		float xOffset = 0;	//Offsets to set the hurtbox
		float yOffset = 0;	//Offsets to set the hurtbox correctly
		while (frame < 4 && framesSinceLast >= attackLengths[frame]) {
			frame++;	//This figures out what frame the player is in
		}
		if (frame >= 4) frame = 3;
		
		if (frame == 1) {
			//This is the main stab
			Rectangle shaftHurtbox;
			Rectangle headHurtbox = new Rectangle(0, 0, 14, 14).setCenter(player.getCenterX(), player.getCenterY());
			//Left - Right direction
			if (dir % 2 == 0) {
				shaftHurtbox = new Rectangle(player.getCenterX(), player.getCenterY(), 18, 6);
				xOffset = (1 - dir) * 5.0f;	//Fancy math for offsetting the hurtbox
				headHurtbox.setPosition(headHurtbox.getX() + (shaftHurtbox.width * (1 - dir)), headHurtbox.getY());
			}
			//Front - Back direction
			else {
				shaftHurtbox = new Rectangle(player.getCenterX(), player.getCenterY(), 6, 18);
				yOffset = (2 - dir) * 5.0f;
				headHurtbox.setPosition(headHurtbox.getX(), headHurtbox.getY() + (shaftHurtbox.height * (2 - dir)));
			}
			shaftHurtbox.setCenter(player.getCenterX(), player.getCenterY());
			shaftHurtbox.setPosition(shaftHurtbox.getX() + xOffset, shaftHurtbox.getY() + yOffset);
			hurtboxes.add(shaftHurtbox);
			hurtboxes.add(headHurtbox);
		}
		else if (frame == 2) {
			//This is the after stab
			Rectangle shaftHurtbox;
			Rectangle headHurtbox = new Rectangle(0, 0, 9, 9).setCenter(player.getCenterX(), player.getCenterY());
			//Left - Right direction
			if (dir % 2 == 0) {
				shaftHurtbox = new Rectangle(player.getCenterX(), player.getCenterY(), 18, 5);
				xOffset = (1 - dir) * 5.0f;	//Fancy math for offsetting the hurtbox
				headHurtbox.setPosition(headHurtbox.getX() + (shaftHurtbox.width * (1 - dir)), headHurtbox.getY());
			}
			//Front - Back direction
			else {
				shaftHurtbox = new Rectangle(player.getCenterX(), player.getCenterY(), 5, 18);
				yOffset = (2 - dir) * 5.0f;
				headHurtbox.setPosition(headHurtbox.getX(), headHurtbox.getY() + (shaftHurtbox.height * (2 - dir)));
			}
			shaftHurtbox.setCenter(player.getCenterX(), player.getCenterY());
			shaftHurtbox.setPosition(shaftHurtbox.getX() + xOffset, shaftHurtbox.getY() + yOffset);
			hurtboxes.add(shaftHurtbox);
			hurtboxes.add(headHurtbox);
		}
		return hurtboxes;
	}

	@Override
	public boolean isFinishedAttacking(PlayerCharacter player, int framesSinceLast) {
		return (framesSinceLast >= 24);
	}

}
