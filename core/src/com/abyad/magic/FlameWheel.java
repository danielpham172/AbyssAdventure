package com.abyad.magic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.projectile.FlameWheelProjectile;
import com.badlogic.gdx.math.Vector2;

public class FlameWheel extends AbstractMagic{
	
	public FlameWheel() {
		super("FLAME WHEEL", "Summons fireballs that spin away from you",
				1, 0, 100, 30, "flameWheel");
	}

	@Override
	public void castMagic(PlayerCharacter player, Vector2 cursorPosition) {
		Vector2 direction = new Vector2(12.0f, 0);
		Vector2 velocity = new Vector2(6.0f, 0);
		velocity.setAngle(5f);
		float centerX = player.getCenterX();
		float centerY = player.getCenterY();
		for (int i = 0; i < 4; i++) {
			FlameWheelProjectile projectile = new FlameWheelProjectile(centerX + direction.x, centerY + direction.y,
					player, centerX, centerY, velocity);
			player.getStage().addActor(projectile);
			
			direction.rotate(90);
			
		}
	}
	
	@Override
	public int magicCircleFrames() {
		return 4;
	}

	@Override
	public boolean usesCursor() {
		return false;
	}
	
	
	@Override
	public boolean spawnsParticles() {
		return true;
	}
}
