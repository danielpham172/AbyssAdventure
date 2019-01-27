package com.abyad.actor.attack;

import java.util.ArrayList;

import com.abyad.actor.cosmetic.CosmeticParticle;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Meditate extends SpecialAttackData {

	private int[] attackLengths = {30, 40};
	
	@Override
	public String getName() {
		return "MEDITATE";
	}
	@Override
	public void initiateAttack(PlayerCharacter player) {
		
	}

	@Override
	public void useAttack(PlayerCharacter player, int framesSinceLast) {
		float particleSpeed = 0.45f;
		float angleRange = 30f;
		float particleLifetime = 20f;
		if (framesSinceLast == 31) {
			player.addPartialMana(1);
			CosmeticParticle.spawnParticle(AbstractSpriteSheet.spriteSheets.get("PARTICLES").getSprite("RED"),
					player.getCenterX(), player.getCenterY() - 4f, 0f, 5f, 1f, 0f, particleSpeed, 0.2f, 0.0f, angleRange,
					0.7f, 0.5f, particleLifetime, 0.2f, 10, player.getStage());
		}
	}

	@Override
	public ArrayList<Rectangle> getHurtboxes(PlayerCharacter player, int framesSinceLast) {
		return new ArrayList<Rectangle>();
	}

	@Override
	public boolean isFinishedAttacking(PlayerCharacter player, int framesSinceLast) {
		return framesSinceLast >= 40;
	}
	
	@Override
	public boolean isHold() {
		return true;
	}

}
