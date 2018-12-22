package com.abyad.actor.tile;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.items.KeyItem;
import com.abyad.interfaces.Interactable;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.stage.PlayStage;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class StairTile extends AbstractTile implements Interactable{

	private static TextureRegion unlockedStairTex = TextureRegion.split(Assets.manager.get(Assets.stairTile), TILE_SIZE, TILE_SIZE)[0][0];
	private static TextureRegion lockedStairTex = TextureRegion.split(Assets.manager.get(Assets.stairTile), TILE_SIZE, TILE_SIZE)[0][1];
	
	private ArrayList<Rectangle> interactBox;
	private boolean isInteractable;
	
	private boolean locked;
	
	public StairTile(int row, int col, boolean locked) {
		super(unlockedStairTex, row, col);
		
		interactBox = new ArrayList<Rectangle>();
		interactBox.add(getBox());
		
		interactables.add(this);
		this.locked = locked;
	}

	@Override
	public void draw(Batch batch, float a) {
		if (locked) {
			tex = lockedStairTex;
		}
		else {
			tex = unlockedStairTex;
		}
		super.draw(batch, a);
		if (isInteractable && !locked) {
			TextureRegion aButton = AbstractSpriteSheet.spriteSheets.get("UI_BUTTONS").getSprite("A");
			float xOrigin = aButton.getRegionWidth() / 2;
			float yOrigin = aButton.getRegionHeight() / 2;
			batch.draw(aButton, getCenter().x - xOrigin, getCenter().y - yOrigin);
		}
		
		isInteractable = false;
	}
	
	@Override
	public ArrayList<Rectangle> getCollisionBox() {
		return new ArrayList<Rectangle>();
	}

	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBox;
	}

	@Override
	public boolean interact(PlayerCharacter source) {
		if (locked) {
			if (source.getHeldItem() instanceof KeyItem) {
				locked = false;
				source.removeHeldItem();
				return true;
			}
		}
		else {
			((PlayStage)getStage()).setReadyForNextLevel(true);
			return true;
		}
		return false;
	}

	@Override
	public void setCanInteract(boolean flag) {
		isInteractable = flag;
	}
	
	@Override
	public boolean remove() {
		interactables.remove(this);
		return super.remove();
	}
}
