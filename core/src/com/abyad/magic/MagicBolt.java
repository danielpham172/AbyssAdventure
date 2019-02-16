package com.abyad.magic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.projectile.MagicBoltProjectile;
import com.badlogic.gdx.math.Vector2;

public class MagicBolt extends AbstractMagic{

	public MagicBolt() {
		super("MAGIC BOLT", "Fires a large arcane projectile",
				1, 0, 100, 20, "magicBolt");
	}

	@Override
	public void castMagic(PlayerCharacter player, Vector2 cursorPosition) {
		Vector2 velocity = new Vector2(cursorPosition.x - player.getCenterX(), cursorPosition.y - player.getCenterY());
		velocity.setLength(4.5f);
		
		MagicBoltProjectile projectile = new MagicBoltProjectile(player.getCenterX(), player.getCenterY(), player, velocity);
		player.getStage().addActor(projectile);
	}

	@Override
	public boolean spawnsParticles() {
		return true;
	}
}
