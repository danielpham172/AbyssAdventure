package com.abyad.actor.attack;

import java.util.ArrayList;

import com.abyad.actor.cosmetic.CosmeticParticle;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Rectangle;

public class Meditate extends SpecialAttackData {

	private static int[] attackLengths = {45, 60};
	private ArrayList<PlayerCharacter> gainedMana = new ArrayList<PlayerCharacter>();
	
	public Meditate() {
		super(Meditate.attackLengths);
	}
	
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
		if (framesSinceLast == attackLengths[0] + 1 && !gainedMana.contains(player)) {
			player.addPartialMana(1);
			CosmeticParticle.spawnParticle(AbstractSpriteSheet.spriteSheets.get("PARTICLES").getSprite("RED"),
					player.getCenterX(), player.getCenterY() - 4f, 0f, 5f, 1f, 0f, particleSpeed, 0.2f, 0.0f, angleRange,
					0.7f, 0.5f, particleLifetime, 0.2f, 10, player.getStage());
			gainedMana.add(player);
		}
	}

	@Override
	public ArrayList<Rectangle> getHurtboxes(PlayerCharacter player, int framesSinceLast) {
		return new ArrayList<Rectangle>();
	}
	
	@Override
	public void reset(PlayerCharacter player) {
		gainedMana.remove(player);
	}
	
	@Override
	public boolean isHold() {
		return true;
	}

}
