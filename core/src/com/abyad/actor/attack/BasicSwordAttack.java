package com.abyad.actor.attack;

import java.util.ArrayList;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.relic.Relic;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BasicSwordAttack extends AttackData{

	private int[] attackLengths = {6, 10, 14, 24};		//The frame thresholds for each sprite. Only the second and third frame are attack frames
	
	@Override
	public String getName() {
		return "SWORD";
	}
	@Override
	public void initiateAttack(PlayerCharacter player) {
		if (player.getState().equals("IDLE")) {
			//If the player was in idle, give a small forward movement. May remove or lower this.
			player.getVelocity().setAngle(((int)(player.getVelocity().angle() + 45) / 90) * 90.0f);
			player.getVelocity().setLength(1.5f);
		}
	}
	
	@Override
	public void useAttack(PlayerCharacter player, int framesSinceLast) {
		player.getVelocity().setLength(player.getVelocity().len() / 1.08f);
		player.move(player.getVelocity());
		ArrayList<Rectangle> hurtboxes = getHurtboxes(player, framesSinceLast);
		ArrayList<AbstractEntity> entities = AbstractEntity.getEntities();
		for (AbstractEntity entity : entities) {
			if (!player.isSameTeam(entity)) {
				ArrayList<Rectangle> otherHitbox = entity.getHitbox();
				if (isOverlapping(hurtboxes, otherHitbox)) {
					//Attacked someone
					Vector2 knockback = new Vector2(entity.getCenterX() - player.getCenterX(), entity.getCenterY() - player.getCenterY());
					knockback.setLength(4.0f);
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
			//This is the initial side swing
			Rectangle hurtbox;
			//Left - Right direction
			if (dir % 2 == 0) {
				hurtbox = new Rectangle(player.getCenterX() - 8, player.getCenterY() - 5, 16, 10);
				xOffset = (1 - dir) * 4.5f;	//Fancy math for offsetting the hurtbox
				yOffset = (dir - 1) * 6;
			}
			//Front - Back direction
			else {
				hurtbox = new Rectangle(player.getCenterX() - 5, player.getCenterY() - 8, 10, 16);
				xOffset = (2 - dir) * 6;
				yOffset = (2 - dir) * 4.5f;
			}
			hurtbox.setPosition(hurtbox.getX() + xOffset, hurtbox.getY() + yOffset);
			hurtboxes.add(hurtbox);
		}
		
		if (frame == 2) {
			//This is the front swing
			Rectangle hurtbox;
			//Left - Right direction
			if (dir % 2 == 0) {
				hurtbox = new Rectangle(player.getCenterX() - 6, player.getCenterY() - 10, 12, 20);
				xOffset = (1 - dir) * 8.0f;
			}
			//Front - Back direction
			else {
				hurtbox = new Rectangle(player.getCenterX() - 10, player.getCenterY() - 6, 20, 12);
				yOffset = (2 - dir) * 8.0f;
			}
			hurtbox.setPosition(hurtbox.getX() + xOffset, hurtbox.getY() + yOffset);
			hurtboxes.add(hurtbox);
		}
		return hurtboxes;
	}

	@Override
	public boolean isFinishedAttacking(PlayerCharacter player, int framesSinceLast) {
		return (framesSinceLast >= 24);
	}

}
