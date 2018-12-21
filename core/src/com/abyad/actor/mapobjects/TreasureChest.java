package com.abyad.actor.mapobjects;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.interfaces.Interactable;
import com.abyad.relic.TonWeightRelic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TreasureChest extends Actor implements Interactable{

	private LootItem item;
	
	private static TextureRegion closedChest;
	private static TextureRegion openChest;
	
	static {
		int rows = 1;
		int cols = 2;
		Texture tex = Assets.manager.get(Assets.treasureChest);
		TextureRegion[][] treasureChestRegions = TextureRegion.split(tex, tex.getWidth() / cols, tex.getHeight() / rows);
		
		closedChest = treasureChestRegions[0][0];
		openChest = treasureChestRegions[0][1];
	}
	
	private FloorTile floor;
	private ArrayList<Rectangle> interactBox;
	private boolean isOpen;
	private boolean isInteractable;
	
	private Rectangle hitbox;
	
	public TreasureChest(FloorTile floor) {
		this.floor = floor;
		
		setOrigin(AbstractTile.TILE_SIZE / 2, AbstractTile.TILE_SIZE / 2);
		setX(floor.getX() - ((AbstractTile.TILE_SIZE - AbstractTile.TILE_LENGTH) / 2) + getOriginX());
		setY(floor.getY() - ((AbstractTile.TILE_SIZE - AbstractTile.TILE_LENGTH) / 2) + getOriginY());
		
		interactBox = new ArrayList<Rectangle>();
		interactBox.add(new Rectangle(floor.getBox()));
		
		hitbox = new Rectangle(floor.getX() + (6 * AbstractTile.TILE_SCALE), floor.getY() + (7 * AbstractTile.TILE_SCALE),
				20 * AbstractTile.TILE_SCALE, 15 * AbstractTile.TILE_SCALE);
		floor.addCollisionBox(hitbox);
		
		item = generateRandomLoot();
		
		interactables.add(this);
	}
	
	public LootItem generateRandomLoot() {
		Vector2 velocity = new Vector2(1, 1);
		velocity.setLength((float)(Math.random() * 3.0f) + 3.0f).setAngle((float)(Math.random() * 360.0f));
		return new RelicLoot(getX(), getY(), velocity, new TonWeightRelic());
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (floor.inView()) {
			if (!isOpen) {
				batch.draw(closedChest, getX() - getOriginX(), getY() - getOriginY(),
						AbstractTile.TILE_SIZE / 2, AbstractTile.TILE_SIZE / 2,
						AbstractTile.TILE_SIZE, AbstractTile.TILE_SIZE, AbstractTile.TILE_SCALE, AbstractTile.TILE_SCALE, getRotation());
			}
			else {
				batch.draw(openChest, getX() - getOriginX(), getY() - getOriginY(),
						AbstractTile.TILE_SIZE / 2, AbstractTile.TILE_SIZE / 2,
						AbstractTile.TILE_SIZE, AbstractTile.TILE_SIZE, AbstractTile.TILE_SCALE, AbstractTile.TILE_SCALE, getRotation());
			}
			if (isInteractable) {
				TextureRegion aButton = AbstractSpriteSheet.spriteSheets.get("UI_BUTTONS").getSprite("A");
				float xOrigin = aButton.getRegionWidth() / 2;
				float yOrigin = aButton.getRegionHeight() / 2;
				batch.draw(aButton, getX() - xOrigin, getY() - yOrigin + (20 * AbstractTile.TILE_SCALE));
			}
		}
		
		isInteractable = false;
		//batch.draw(AbstractTile.box, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}
	
	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBox;
	}

	@Override
	public void interact(PlayerCharacter source) {
		if (!isOpen) {
			isOpen = true;
			interactBox.clear();
			
			item.spawn();
			getStage().addActor(item);
		}
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
