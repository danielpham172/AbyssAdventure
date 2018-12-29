package com.abyad.actor.mapobjects.items;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ManaCapsuleItem extends LootItem{

	private ArrayList<Rectangle> interactBox;
	private ArrayList<Rectangle> temporaryCollideBox;
	
	private static Rectangle baseBox = new Rectangle(0, 0, 16, 16);
	
	private boolean markForRemoval;
	
	public ManaCapsuleItem(float x, float y, Vector2 velocity) {
		super(x, y, velocity, AbstractSpriteSheet.spriteSheets.get("CAPSULES").getSprite("MANA_CAPSULE"));
		
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
	public boolean interact(PlayerCharacter source) {
		if (!markForRemoval) {
			if (source.getMaxMana() < 20) {
				source.modifyMaxMana(1);
				source.addPartialMana(4);
			}
			else {
				source.addPartialMana(source.getMaxMana() * 4);
			}
			markForRemoval = true;
			return true;
		}
		return false;
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
