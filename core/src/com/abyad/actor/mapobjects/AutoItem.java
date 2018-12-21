package com.abyad.actor.mapobjects;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AutoItem extends MapItem{
	
	public AutoItem(TextureRegion tex, float followRadius, boolean following) {
		super(tex, followRadius);
		setFollowing(following);
	}
	
	@Override
	public void act(float delta) {
		if (velocity.len() == 0f) following = true;
		super.act(delta);
		
		updateCollideAndInteractBox();
		ArrayList<PlayerCharacter> players = PlayerCharacter.getPlayers();
		for (PlayerCharacter player : players) {
			if (isOverlapping(player.getCollideBox(), getCollideBox())) {
				playerPickup(player);
				remove();
				return;
			}
		}
	}

	public abstract void playerPickup(PlayerCharacter player);
}
