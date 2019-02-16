package com.abyad.magic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.projectile.DarkSpiralProjectile;
import com.badlogic.gdx.math.Vector2;

public class DarkSpiral extends AbstractMagic{

	public DarkSpiral() {
		super("DARK SPIRAL", "Create a black hole that pulls enemy into it",
				1, 0, 120, 30, "darkSpiral");
	}

	@Override
	public void castMagic(PlayerCharacter player, Vector2 cursorPosition) {
		DarkSpiralProjectile projectile = new DarkSpiralProjectile(cursorPosition.x, cursorPosition.y, player);
		player.getStage().addActor(projectile);
	}
	
	@Override
	public int magicCircleFrames() {
		return 3;
	}
}
