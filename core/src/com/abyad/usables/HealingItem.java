package com.abyad.usables;

import com.abyad.actor.cosmetic.CosmeticParticle;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HealingItem extends InventoryItem{

	private int restore;
	
	public HealingItem(String name, int restore, TextureRegion tex) {
		super(name, "Restores " + restore + " HP", tex);
		this.restore = restore;
	}

	@Override
	public boolean canUse(PlayerCharacter player) {
		return getCount() > 0 && player.getHP() < player.getMaxHP();
	}

	@Override
	public void use(PlayerCharacter player) {
		player.modifyHP(restore);
		CosmeticParticle.spawnParticle(AbstractSpriteSheet.spriteSheets.get("PICKUPS").getSprite("HEART"),
				player.getCenterX(), player.getCenterY() - 4f, 0f, 8f, 2f, 0f, 0.4f, 0.2f, 0.0f, 20, 0.75f, 0.5f, 50, 0.2f,
				restore + 2, player);
	}
}
