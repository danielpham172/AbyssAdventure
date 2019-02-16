package com.abyad.actor.tile;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * The WallTile class is derived from the AbstractTile class. Used to draw the various walls of the map.
 *
 */
public class WallTile extends AbstractTile{
	
	private boolean isFrontWall = false;	//Used to check if it's one of those
	private ArrayList<Rectangle> collisionBox;
	
	public WallTile(TextureRegion tex, int row, int col, float rotation, boolean isFrontWall) {
		super(tex, row, col, rotation);
		this.isFrontWall = isFrontWall;
		
		collisionBox = new ArrayList<Rectangle>();
		collisionBox.add(getBox());
	}
	
	/**
	 * Tells if the wall tile is a "front wall" (the full faced wall tiles).
	 * @return
	 */
	public boolean isFrontWall() {
		return isFrontWall;
	}
	
	@Override
	public ArrayList<Rectangle> getCollisionBox(){
		return collisionBox;
	}
}
