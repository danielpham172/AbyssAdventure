package com.abyad.magic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.projectile.RockProjectile;
import com.badlogic.gdx.math.Vector2;

public class RockScatter extends AbstractMagic{

	private static float RANDOM_ANGLE = 45f;
	private static float SPEED = 3.2f;
	private static float RANDOM_SPEED = 0.8f;
	private static float PRECISION_FACTOR = 1.5f;
	
	public RockScatter() {
		super("ROCK SCATTER", "Shoot many small rock pellets",
				1, 0, 140, 20, "rockScatter");
	}

	@Override
	public void castMagic(PlayerCharacter player, Vector2 cursorPosition) {
		for (int i = 0; i < 18; i++) {
			Vector2 velocity = new Vector2(cursorPosition.x - player.getCenterX(), cursorPosition.y - player.getCenterY());
			//Angle
			float randomAngle = velocity.angle();
			randomAngle += ((Math.random() < 0.5) ? -1 : 1) * (float)Math.pow(Math.random(), PRECISION_FACTOR) * RANDOM_ANGLE;
			float randomSpeed = SPEED;
			randomSpeed += ((Math.random() < 0.5) ? -1 : 1) * (float)Math.pow(Math.random(), PRECISION_FACTOR) * RANDOM_SPEED;
			velocity.setLength(randomSpeed);
			velocity.setAngle(randomAngle);
			
			RockProjectile projectile = new RockProjectile(player.getCenterX(), player.getCenterY(), player, velocity);
			player.getStage().addActor(projectile);
		}
	}

	@Override
	public boolean spawnsParticles() {
		return true;
	}
}
