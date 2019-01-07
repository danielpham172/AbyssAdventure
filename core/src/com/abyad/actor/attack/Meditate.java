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
	public void initiateAttack(PlayerCharacter player) {
		
	}

	@Override
	public void useAttack(PlayerCharacter player, int framesSinceLast) {
		float particleSpeed = 0.45f;
		float maxParticleAngle = 30f;
		float particleLifetime = 20f;
		if (framesSinceLast == 31) {
			player.addPartialMana(1);
			for (int count = 0; count < 10; count++) {
				Vector2 pointing = new Vector2(0, particleSpeed * (float)((Math.random() * 0.4) + 0.8f));
				pointing.rotate(maxParticleAngle - (float)(Math.random() * maxParticleAngle * 2));
				int lifetime = (int)(((Math.random() * 0.4) + 0.8) * particleLifetime);
				float randomXModifier = (float)(Math.random() * 10f) - 5f;
				float randomYModifier = (float)(Math.random() * 2f) - 1f;
				CosmeticParticle particle = new CosmeticParticle(AbstractSpriteSheet.spriteSheets.get("PARTICLES").getSprite("RED"),
						player.getCenterX() + randomXModifier, player.getCenterY() - 4f + randomYModifier, 0.0f,
						new Vector2(pointing.x, 0), pointing.y, lifetime);
				particle.setTransparency(0.5f);
				particle.setScale(0.7f);
				
				player.getStage().addActor(particle);
			}
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
