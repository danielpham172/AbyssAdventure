package com.abyad.actor.mapobjects;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.items.GoldItem;
import com.abyad.actor.mapobjects.items.HeartItem;
import com.abyad.actor.mapobjects.items.KeyItem;
import com.abyad.actor.mapobjects.items.LifeCapsuleItem;
import com.abyad.actor.mapobjects.items.LootItem;
import com.abyad.actor.mapobjects.items.ManaCapsuleItem;
import com.abyad.actor.mapobjects.items.MapItem;
import com.abyad.actor.mapobjects.items.RelicLoot;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.relic.TonWeightRelic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class RareTreasureChest extends TreasureChest{

	private static TextureRegion closedChest = AbstractSpriteSheet.spriteSheets.get("CHEST").getSprite("RARE_CLOSED");
	private static TextureRegion openChest = AbstractSpriteSheet.spriteSheets.get("CHEST").getSprite("RARE_OPEN");
	
	private int shakeLength;
	
	public RareTreasureChest(FloorTile floor) {
		super(floor);
		
		generateRandomLoot();
		if (Math.random() < 0.4) generateRandomLoot();
		generateRareLoot();
	}
	
	@Override
	public void act(float delta) {
		if (shakeLength > 0) shakeLength--;
	}
	
	public void generateRareLoot() {
		int choice = (int)(Math.random() * 4);
		if (choice == 0){
			int heartAmount = (int)(Math.random() * 6) + 5;
			for (int i = 0; i <= heartAmount; i++) {
				Vector2 velocity = new Vector2(1, 0);
				velocity.setLength((float)(Math.random() * 2.0f) + 3.0f).setAngle((float)(Math.random() * 360.0f));
				items.add(new HeartItem(getX(), getY(), velocity));
			}
		}
		else if (choice == 1){
			Vector2 velocity = new Vector2(1, 0);
			velocity.setLength((float)(Math.random() * 2.0f) + 3.0f).setAngle((float)(Math.random() * 360.0f));
			items.add(new LifeCapsuleItem(getX(), getY(), velocity));
		}
		else if (choice == 2){
			Vector2 velocity = new Vector2(1, 0);
			velocity.setLength((float)(Math.random() * 2.0f) + 3.0f).setAngle((float)(Math.random() * 360.0f));
			items.add(new ManaCapsuleItem(getX(), getY(), velocity));
		}
		else{
			int goldAmount = (int)(Math.random() * 16) + 20;
			for (int i = 0; i <= goldAmount; i++) {
				Vector2 velocity = new Vector2(1, 0);
				velocity.setLength((float)(Math.random() * 2.0f) + 2.0f).setAngle((float)(Math.random() * 360.0f));
				items.add(new GoldItem(getX(), getY(), velocity));
			}
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (floor.inView()) {
			if (!isOpen) {
				batch.draw(closedChest, getX() - getOriginX() + (((((shakeLength + 2) % 5) - 2)) * (1.5f * AbstractTile.TILE_SCALE)), getY() - getOriginY(),
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
	public boolean interact(PlayerCharacter source) {
		if (!isOpen) {
			if (source.getHeldItem() instanceof KeyItem) {
				isOpen = true;
				source.removeHeldItem();
				interactBox.clear();
				for (MapItem item : items) {
					if (item instanceof LootItem) ((LootItem)item).spawn();
					getStage().addActor(item);
				}
				return true;
			}
			else {
				shakeLength = 30;
				return false;
			}
		}
		return false;
	}

}
