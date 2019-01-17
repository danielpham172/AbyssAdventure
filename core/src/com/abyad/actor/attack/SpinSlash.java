package com.abyad.actor.attack;

import java.util.ArrayList;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.relic.Relic;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SpinSlash extends SpecialAttackData{

	private int[] attackLengths = {4, 10, 16, 22, 28, 34};
	
	@Override
	public String getName() {
		return "SPIN SLASH";
	}
	@Override
	public void initiateAttack(PlayerCharacter player) {
		if (player.getState().equals("IDLE")) {
			//If the player was in idle, give a small forward movement. May remove or lower this.
			player.getVelocity().setAngle(((int)(player.getVelocity().angle() + 45) / 90) * 90.0f);
			player.getVelocity().setLength(0.5f);
		}
		else if (player.getState().equals("MOVE")) {
			player.getVelocity().scl(2.0f);
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
					knockback.setLength(5.0f);
					int kbLength = 8;
					int damage = 1;
					HitEvent event = new HitEvent(player, entity, damage, knockback, kbLength);
					
					//Activate relic hit effects (modifies the event)
					for (Relic relic : player.getRelics()) {
						if (!relic.isOnCooldown() && Math.random() < relic.getActivationRate()) {
							relic.onHit(player, event, entity);
						}
					}
					
					entity.takeDamage(event);
				}
			}
		}
		
		int frame = 0;
		while (frame < attackLengths.length && framesSinceLast >= attackLengths[frame]) {
			frame++;	//This figures out what frame the player is in
		}
		if (frame >= attackLengths.length) frame = attackLengths.length - 1;
		player.setHeight((-(float)Math.pow((2 - frame), 2) + 9) * 0.25f);
		if (frame == 5) player.setHeight(0);
		
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
		while (frame < attackLengths.length && framesSinceLast >= attackLengths[frame]) {
			frame++;	//This figures out what frame the player is in
		}
		if (frame >= attackLengths.length) frame = attackLengths.length - 1;
		
		if (frame != 5) dir = (dir + frame) % 4;
		
		if (frame == 5) {
			Rectangle hurtbox = new Rectangle(player.getCenterX() - 8, player.getCenterY() - 8, 16, 16);
			//Left - Right direction
			if (dir % 2 == 0) {
				xOffset = (1 - dir) * 6.0f;
				yOffset = (dir - 1) * 6.0f;
			}
			//Front - Back direction
			else {
				xOffset = (2 - dir) * 6.0f;
				yOffset = (2 - dir) * 6.0f;
			}
			hurtbox.setPosition(hurtbox.getX() + xOffset, hurtbox.getY() + yOffset);
			hurtboxes.add(hurtbox);
		}
		else if (frame != 0){
			Rectangle hurtbox;
			//Left - Right direction
			if (dir % 2 == 0) {
				hurtbox = new Rectangle(player.getCenterX() - 16, player.getCenterY() - 8, 32, 16);
				yOffset = (dir - 1) * 6.0f;
			}
			//Front - Back direction
			else {
				hurtbox = new Rectangle(player.getCenterX() - 8, player.getCenterY() - 16, 16, 32);
				xOffset = (2 - dir) * 6.0f;
			}
			hurtbox.setPosition(hurtbox.getX() + xOffset, hurtbox.getY() + yOffset);
			hurtboxes.add(hurtbox);
		}
		return hurtboxes;
	}

	@Override
	public boolean isFinishedAttacking(PlayerCharacter player, int framesSinceLast) {
		return (framesSinceLast >= 34);
	}
	
	@Override
	public int getRequiredMana() {
		return 1;
	}

}
