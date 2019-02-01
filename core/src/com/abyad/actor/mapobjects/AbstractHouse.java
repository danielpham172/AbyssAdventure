package com.abyad.actor.mapobjects;

import java.util.ArrayList;

import com.abyad.actor.cosmetic.DeathAnimation;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.interfaces.Interactable;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class AbstractHouse extends Actor implements Interactable{

	private FloorTile floor;
	private TextureRegion tex;
	private boolean initialized;
	private ArrayList<Rectangle> interactBoxes;
	private boolean isInteractable;
	
	/**
	 * House covers a 3x3 tile, probably going to make the bottom center be placed on the floor tile
	 * Hitboxes:
	 * Name					Center Offsets			Dims
	 * Bottom-Center		0 1.5					32 29
	 * Bottom-Left			0.5 1.5					31 29
	 * Bottom-Right			-0.5 1.5				31 29
	 * Middle-Center		0 0						32 32
	 * Middle-Left			0.5 0					31 32
	 * Middle-Right			-0.5 0					31 32		
	 * Top-Center			0 -6					32 20
	 * Top-Left				0.5 0					31 20
	 * Top-Right			-0.5 0					31 20
	 */
	
	private static float[] xOffsets = {1.0f, 0f, -1.0f};
	private static float[] yOffsets = {1.5f, 0f, -10f};
	private static float[] widths = {30, 32, 30};
	private static float[] heights = {29, 32, 12};
	
	protected static final float FONT_SCALE = 0.25f;
	protected static BitmapFont font = Assets.manager.get(Assets.font);
	
	public AbstractHouse(FloorTile floor) {
		this.floor = floor;
		
		setOriginX(getClosedHouse().getRegionWidth() / 2);
		setOriginY(getClosedHouse().getRegionHeight() / 2);
		setPosition(floor.getCenter().x, floor.getCenter().y + AbstractTile.TILE_LENGTH);
		
		interactBoxes = new ArrayList<Rectangle>();
		Rectangle interactBox = new Rectangle(floor.getBox());
		interactBox.set(interactBox.getX(), interactBox.getY() - AbstractTile.TILE_LENGTH / 2, interactBox.getWidth(), interactBox.getHeight());
		interactBoxes.add(interactBox);
	}
	
	public void initialize() {
		if (!initialized) {
			for (int row = 0; row <= 2; row++) {
				for (int col = -1; col <= 1; col++) {
					AbstractTile tile = floor.getNearbyTile(row, col);
					Rectangle box = new Rectangle(0, 0,
							widths[col + 1] * AbstractTile.TILE_SCALE, heights[row] * AbstractTile.TILE_SCALE);
					box.setCenter(tile.getCenter().x + (xOffsets[col + 1] * AbstractTile.TILE_SCALE),
							tile.getCenter().y + (yOffsets[row] * AbstractTile.TILE_SCALE));
					
					tile.getCollisionBox().add(box);
				}
			}
		}
		initialized = true;
		interactables.add(this);
	}
	
	
	@Override
	public void draw(Batch batch, float a) {
		if (!isInteractable) {
			tex = getClosedHouse();
		}
		else {
			tex = getOpenHouse();
		}
		batch.draw(tex, getX() - getOriginX(), super.getY() - getOriginY(),
				getOriginX(), getOriginY(), tex.getRegionWidth(), tex.getRegionHeight(),
				AbstractTile.TILE_SCALE, AbstractTile.TILE_SCALE, getRotation());
		if (isInteractable) {
			font.getData().setScale(FONT_SCALE);
			float fontX = floor.getCenter().x - (getText().width / 2);
			float fontY = floor.getCenter().y + (AbstractTile.TILE_SIZE / 2) - (getText().height / 2);
			font.draw(batch, getText(), fontX, fontY);
			font.getData().setScale(1.0f);
		}
		
		isInteractable = false;
	}
	
	public abstract TextureRegion getClosedHouse();
	public abstract TextureRegion getOpenHouse();
	public abstract GlyphLayout getText();
	
	@Override
	public boolean remove() {
		interactables.remove(this);
		return super.remove();
	}
	
	@Override
	public float getY() {
		return super.getY() - 20;
	}

	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBoxes;
	}

	@Override
	public boolean interact(PlayerCharacter source) {
		for (PlayerCharacter player : PlayerCharacter.getPlayers()) {
			DeathAnimation deathAnim = new DeathAnimation(player.getCenterX(), player.getCenterY());
			player.getStage().addActor(deathAnim);
			player.markForRemoval();
		}
		return true;
	}

	@Override
	public void setCanInteract(boolean flag) {
		isInteractable = true;
	}
}
