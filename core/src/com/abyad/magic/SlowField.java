package com.abyad.magic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.projectile.SlowFieldProjectile;
import com.badlogic.gdx.math.Vector2;

public class SlowField extends AbstractMagic{

	public SlowField() {
		super("SLOW FIELD", "Makes a large area that slows enemies that inside it",
				1, 0, 120, 30, "slowField");
	}

	@Override
	public void castMagic(PlayerCharacter player, Vector2 cursorPosition) {
		SlowFieldProjectile projectile = new SlowFieldProjectile(cursorPosition.x, cursorPosition.y, player);
		player.getStage().addActor(projectile);
	}
	
	@Override
	public int magicCircleFrames() {
		return 6;
	}
}
