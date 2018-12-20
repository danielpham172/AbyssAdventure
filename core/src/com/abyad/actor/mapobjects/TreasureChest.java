package com.abyad.actor.mapobjects;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.interfaces.Interactable;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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
	
	private Rectangle hitbox;
	
	public TreasureChest(FloorTile floor) {
		this.floor = floor;
		
		setOrigin(AbstractTile.TILE_SIZE / 2, AbstractTile.TILE_SIZE / 2);
		setX(floor.getX() + getOriginX());
		setY(floor.getY() + getOriginY());
		
		interactBox = new ArrayList<Rectangle>();
		interactBox.add(new Rectangle(floor.getBox()));
		
		hitbox = new Rectangle(floor.getX() + (6 * AbstractTile.TILE_SCALE), floor.getY() + (7 * AbstractTile.TILE_SCALE),
				20 * AbstractTile.TILE_SCALE, 15 * AbstractTile.TILE_SCALE);
		floor.addCollisionBox(hitbox);
		
		interactables.add(this);
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (floor.inView()) {
			if (!isOpen) {
				batch.draw(closedChest, getX() - ((AbstractTile.TILE_SIZE - AbstractTile.TILE_LENGTH) / 2) - getOriginX(),
						getY() - ((AbstractTile.TILE_SIZE - AbstractTile.TILE_LENGTH) / 2) - getOriginY(),
						AbstractTile.TILE_SIZE / 2, AbstractTile.TILE_SIZE / 2,
						AbstractTile.TILE_SIZE, AbstractTile.TILE_SIZE, AbstractTile.TILE_SCALE, AbstractTile.TILE_SCALE, getRotation());
			}
			else {
				batch.draw(openChest, getX() - ((AbstractTile.TILE_SIZE - AbstractTile.TILE_LENGTH) / 2) - getOriginX(),
						getY() - ((AbstractTile.TILE_SIZE - AbstractTile.TILE_LENGTH) / 2) - getOriginY(),
						AbstractTile.TILE_SIZE / 2, AbstractTile.TILE_SIZE / 2,
						AbstractTile.TILE_SIZE, AbstractTile.TILE_SIZE, AbstractTile.TILE_SCALE, AbstractTile.TILE_SCALE, getRotation());
			}
		}
		
		//batch.draw(AbstractTile.box, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}
	
	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBox;
	}

	@Override
	public void interact(PlayerCharacter source) {
		isOpen = true;
		interactBox.clear();
	}
	
	@Override
	public boolean remove() {
		interactables.remove(this);
		return super.remove();
	}

}
