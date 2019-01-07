package com.abyad.magic;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.projectile.HealingFieldProjectile;
import com.badlogic.gdx.math.Vector2;

public class HealingField extends AbstractMagic{

	public HealingField() {
		super("HEALING FIELD", "Creates a field that restores HP to allies who enter it",
				3, 0, 300, 40, "healingField");
	}

	@Override
	public void castMagic(PlayerCharacter player, Vector2 cursorPosition) {
		HealingFieldProjectile projectile = new HealingFieldProjectile(cursorPosition.x, cursorPosition.y, player);
		player.getStage().addActor(projectile);
	}
	
	@Override
	public int magicCircleFrames() {
		return 4;
	}
}
