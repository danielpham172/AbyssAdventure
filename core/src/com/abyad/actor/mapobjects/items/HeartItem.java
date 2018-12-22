package com.abyad.actor.mapobjects.items;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class HeartItem extends AutoItem{

	private ArrayList<Rectangle> collideBox;
	private ArrayList<Rectangle> temporaryCollideBox;
	
	private static Rectangle baseBox = new Rectangle(0, 0, 5, 5);
	
	public HeartItem(float x, float y, Vector2 velocity) {
		super(AbstractSpriteSheet.spriteSheets.get("PICKUPS").getSprite("HEART"), 80, false);
		this.velocity = velocity;
		setX(x);
		setY(y);
		
		collideBox = new ArrayList<Rectangle>();
		temporaryCollideBox = new ArrayList<Rectangle>();
		
		Rectangle box = new Rectangle(baseBox);
		Rectangle tempBox = new Rectangle(baseBox);
		
		collideBox.add(box);
		temporaryCollideBox.add(tempBox);
		
		updateCollideAndInteractBox();
	}

	@Override
	public void playerPickup(PlayerCharacter player) {
		player.restoreHealth(1);
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

}
