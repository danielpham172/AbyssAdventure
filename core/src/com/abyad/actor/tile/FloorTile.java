package com.abyad.actor.tile;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * The FloorTile class is derived from the AbstractTile class and is used to draw the floors of the map
 *
 */
public class FloorTile extends AbstractTile{
	
	private ArrayList<Rectangle> collisionBox = new ArrayList<Rectangle>();
	
	/**
	 * Constructs a FloorTile at a row and column. Randomly selects a texture and rotates it randomly
	 * @param row		Row of the tile
	 * @param col		Column of the tile
	 */
	
	public FloorTile(TextureRegion tex, int row, int col, float rotation) {
		super(tex, row, col, rotation);
	}

	@Override
	public ArrayList<Rectangle> getCollisionBox() {
		// TODO Auto-generated method stub
		return collisionBox;
	}
	
	public void addCollisionBox(Rectangle box) {
		collisionBox.add(box);
	}
}
