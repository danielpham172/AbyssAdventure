package com.abyad.relic;

import com.abyad.actor.attack.AttackData;
import com.abyad.actor.cosmetic.CosmeticParticle;
import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Vector2;

public class BlackBeltRelic extends Relic{

	private static int priorityNumber = -1;
	static {
		priorityNumber = findPriorityNumber(BlackBeltRelic.class);
	}
	
	public BlackBeltRelic() {
		super("BLACK BELT", "Chance to deal double damage on non-magic attacks", 0.08f, AbstractSpriteSheet.spriteSheets.get("RELICS").getSprite("BLACK_BELT"));
	}

	@Override
	public float getActivationRate() {
		return Math.min(super.getActivationRate() * (0.5f * (getCount() + 1)), 1.0f);
	}
	
	@Override
	public void onHit(PlayerCharacter player, AttackData attackType, HitEvent attack, AbstractEntity hit) {
		float particleSpeed = 1.7f;
		float angleRange = 60f;
		float particleLifetime = 12f;
		float angleBlow = (new Vector2(hit.getCenterX() - player.getCenterX(), hit.getCenterY() - player.getCenterY())).angle();
		CosmeticParticle.spawnParticle(AbstractSpriteSheet.spriteSheets.get("PARTICLES").getSprite("BLACK"),
				hit.getCenterX(), hit.getCenterY() - 1.2f, 0f, 1f, 1f, 0f, particleSpeed, 0.2f, angleBlow - 90f, angleRange,
				0.6f, 1.0f, particleLifetime, 0.2f, 20, player.getStage());
		attack.setDamage(attack.getDamage() * 2);
	}

	@Override
	public int getPriority() {
		return priorityNumber;
	}
	
}
