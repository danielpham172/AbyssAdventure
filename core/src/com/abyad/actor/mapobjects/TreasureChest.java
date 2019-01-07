package com.abyad.actor.mapobjects;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.items.GoldItem;
import com.abyad.actor.mapobjects.items.HeartItem;
import com.abyad.actor.mapobjects.items.KeyItem;
import com.abyad.actor.mapobjects.items.LootItem;
import com.abyad.actor.mapobjects.items.ManaItem;
import com.abyad.actor.mapobjects.items.MapItem;
import com.abyad.actor.mapobjects.items.RelicLoot;
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

	protected ArrayList<MapItem> items;
	
	private static TextureRegion closedChest = AbstractSpriteSheet.spriteSheets.get("CHEST").getSprite("NORMAL_CLOSED");
	private static TextureRegion openChest = AbstractSpriteSheet.spriteSheets.get("CHEST").getSprite("NORMAL_OPEN");
	
	protected FloorTile floor;
	protected ArrayList<Rectangle> interactBox;
	protected boolean isOpen;
	protected boolean isInteractable;
	
	protected Rectangle hitbox;
	
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
		
		items = new ArrayList<MapItem>();
		generateRandomLoot();
		if (Math.random() < 0.2) generateRandomLoot();
		
		interactables.add(this);
	}
	
	public void generateRandomLoot() {
		int choice = (int)(Math.random() * 4);
		if (choice == 0) {
			Vector2 velocity = new Vector2(1, 0);
			velocity.setLength((float)(Math.random() * 2.0f) + 3.0f).setAngle((float)(Math.random() * 360.0f));
			items.add(new RelicLoot(getX(), getY(), velocity, new TonWeightRelic()));
		}
		else if (choice == 1){
			int heartAmount = (int)(Math.random() * 3) + 1;
			for (int i = 0; i < heartAmount; i++) {
				Vector2 velocity = new Vector2(1, 0);
				velocity.setLength((float)(Math.random() * 2.0f) + 3.0f).setAngle((float)(Math.random() * 360.0f));
				items.add(new HeartItem(getX(), getY(), velocity));
			}
		}
		else if (choice == 2) {
			int manaAmount = (int)(Math.random() * 5) + 4;
			for (int i = 0; i < manaAmount; i++) {
				Vector2 velocity = new Vector2(1, 0);
				velocity.setLength((float)(Math.random() * 2.0f) + 3.0f).setAngle((float)(Math.random() * 360.0f));
				items.add(new ManaItem(getX(), getY(), velocity));
			}
		}
		else{
			int goldAmount = (int)(Math.random() * 5) + 5;
			for (int i = 0; i < goldAmount; i++) {
				Vector2 velocity = new Vector2(1, 0);
				velocity.setLength((float)(Math.random() * 2.0f) + 2.0f).setAngle((float)(Math.random() * 360.0f));
				items.add(new GoldItem(getX(), getY(), velocity));
			}
		}
		//return new KeyItem(getX(), getY(), velocity);
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
	public boolean interact(PlayerCharacter source) {
		if (!isOpen) {
			isOpen = true;
			interactBox.clear();
			
			for (MapItem item : items) {
				if (item instanceof LootItem) ((LootItem)item).spawn();
				getStage().addActor(item);
			}
			return true;
		}
		return false;
	}
	
	public void addItem(MapItem item) {
		item.setPosition(getX(), getY());
		item.getVelocity().setLength((float)(Math.random() * 2.0f) + 3.0f)
			.setAngle((float)(Math.random() * 360.0f)).setAngle((float)(Math.random() * 360.0f));
		items.add(item);
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
