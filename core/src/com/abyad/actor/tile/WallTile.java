package com.abyad.actor.tile;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.abyad.sprite.EnvironmentSprite;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * The WallTile class is derived from the AbstractTile class. Used to draw the various walls of the map.
 *
 */
public class WallTile extends AbstractTile{
	
	private boolean isFrontWall = false;	//Used to check if it's one of those
	private ArrayList<Rectangle> collisionBox;
	
	/**
	 * Constructs a WallTile with the given row and col. The surroundings is a 3x3 int array of the map around the tile to be made
	 * so the current texture is used.
	 * @param row				The row of the tile
	 * @param col				The column of the tile
	 * @param surroundings		The surroundings around the tiles to help select the right
	 */
	public WallTile(int row, int col, int[][] surroundings, EnvironmentSprite environment) {
		super(environment.getWallSprite(surroundings), row, col);
		setRotation(setCorrectRotation(surroundings));
		
		collisionBox = new ArrayList<Rectangle>();
		collisionBox.add(getBox());
	}
	
	/**
	 * Sets the rotation of the tile to get the sprite matching
	 * @param s			The surroundings
	 * @return
	 */
	private int setCorrectRotation(int[][] s) {
		if (s[0][1] == 0) {	
			isFrontWall = true;
			return 0;
		}
		else if (s[2][1] == 0) {
			return 0;
		}
		else if (s[1][0] == 0) {	//Open to the left
			//Rotate 90 degrees
			return 90;
		}
		else if (s[1][2] == 0 || s[2][2] == 0) {	//Open to the right and corner
			//Rotate -90 degrees
			return -90;
		}
		else return 0;
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
