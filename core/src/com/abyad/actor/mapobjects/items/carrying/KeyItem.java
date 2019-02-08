package com.abyad.actor.mapobjects.items.carrying;

import java.util.ArrayList;

import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class KeyItem extends CarryingItem{

	private ArrayList<Rectangle> interactBox;
	private ArrayList<Rectangle> temporaryCollideBox;
	
	private static Rectangle baseBox = new Rectangle(0, 0, 16, 16);
	
	private boolean markForRemoval;
	
	public KeyItem(float x, float y, Vector2 velocity) {
		super(x, y, velocity, AbstractSpriteSheet.spriteSheets.get("CARRY").getSprite("KEY"), 1.0f);
		
		interactBox = new ArrayList<Rectangle>();
		temporaryCollideBox = new ArrayList<Rectangle>();
		
		Rectangle box = new Rectangle(baseBox);
		Rectangle tempBox = new Rectangle(baseBox);
		
		interactBox.add(box);
		temporaryCollideBox.add(tempBox);
		
		updateCollideAndInteractBox();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (markForRemoval) remove();
	}
	
	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBox;
	}

	@Override
	public void updateCollideAndInteractBox() {
		Rectangle box = interactBox.get(0);
		box.setPosition(getX() - (box.getWidth() / 2), getY() - (box.getHeight() / 2));
	}

	@Override
	public ArrayList<Rectangle> getCollideBox() {
		return interactBox;
	}

	@Override
	public ArrayList<Rectangle> getCollideBox(float x, float y) {
		temporaryCollideBox.get(0).setPosition(x - (temporaryCollideBox.get(0).getWidth() / 2), y - (temporaryCollideBox.get(0).getHeight() / 2));
		return temporaryCollideBox;
	}

}
