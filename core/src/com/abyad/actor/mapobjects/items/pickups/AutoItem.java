package com.abyad.actor.mapobjects.items.pickups;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.items.MapItem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class AutoItem extends MapItem{
	
	private ArrayList<Rectangle> collideBox;
	private ArrayList<Rectangle> temporaryCollideBox;
	
	private static Rectangle baseBox = new Rectangle(0, 0, 5, 5);
	
	public AutoItem(TextureRegion tex, float followRadius, boolean following) {
		super(tex, followRadius);
		setFollowing(following);
		collideBox = new ArrayList<Rectangle>();
		temporaryCollideBox = new ArrayList<Rectangle>();
		
		Rectangle box = new Rectangle(baseBox);
		Rectangle tempBox = new Rectangle(baseBox);
		
		collideBox.add(box);
		temporaryCollideBox.add(tempBox);
		
		updateCollideAndInteractBox();
	}
	
	@Override
	public void act(float delta) {
		if (velocity.len() == 0f) following = true;
		super.act(delta);
		
		updateCollideAndInteractBox();
		if (following) {
			ArrayList<PlayerCharacter> players = PlayerCharacter.getPlayers();
			for (PlayerCharacter player : players) {
				if (isOverlapping(player.getCollideBox(), getCollideBox())) {
					playerPickup(player);
					remove();
					return;
				}
			}
		}
	}
	
	@Override
	public void updateCollideAndInteractBox() {
		Rectangle box = collideBox.get(0);
		box.setPosition(getX() - (box.getWidth() / 2), getY() - (box.getHeight() / 2));
	}

	@Override
	public ArrayList<Rectangle> getCollideBox() {
		return collideBox;
	}

	@Override
	public ArrayList<Rectangle> getCollideBox(float x, float y) {
		temporaryCollideBox.get(0).setPosition(x - (temporaryCollideBox.get(0).getWidth() / 2), y - (temporaryCollideBox.get(0).getHeight() / 2));
		return temporaryCollideBox;
	}

	public abstract void playerPickup(PlayerCharacter player);
}
