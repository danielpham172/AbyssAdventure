package com.abyad.actor.tile;

import java.util.LinkedHashMap;

import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The WallTile class is derived from the AbstractTile class. Used to draw the various walls of the map.
 *
 */
public class WallTile extends AbstractTile{

	//List of the different wall textures
	private static LinkedHashMap<String, TextureRegion> wallTextures = new LinkedHashMap<String, TextureRegion>();
	
	static {
		Texture wall = Assets.manager.get(Assets.wallTiles);
		TextureRegion[] wallTiles = TextureRegion.split(wall, wall.getWidth() / 11, wall.getHeight())[0];
		
		wallTextures.put("SINGLE", wallTiles[0]);
		wallTextures.put("CORNER", wallTiles[1]);
		wallTextures.put("BENT-R", wallTiles[2]);
		wallTextures.put("BENT-L", wallTiles[3]);
		wallTextures.put("BENT-U", wallTiles[4]);
		wallTextures.put("FRONT", wallTiles[5]);
		wallTextures.put("FRONT+R", wallTiles[6]);
		wallTextures.put("FRONT+L", wallTiles[7]);
		wallTextures.put("FRONT+R+L", wallTiles[8]);
		wallTextures.put("CORNER_FRONT-R", wallTiles[9]);
		wallTextures.put("CORNER_FRONT-L", wallTiles[10]);
	}
	
	private boolean isFrontWall = false;	//Used to check if it's one of those
	
	/**
	 * Constructs a WallTile with the given row and col. The surroundings is a 3x3 int array of the map around the tile to be made
	 * so the current texture is used.
	 * @param row				The row of the tile
	 * @param col				The column of the tile
	 * @param surroundings		The surroundings around the tiles to help select the right
	 */
	public WallTile(int row, int col, int[][] surroundings) {
		super(wallTextures.get(setCorrectWall(surroundings)), row, col);
		setRotation(setCorrectRotation(surroundings));
	}
	
	/**
	 * Selects the texture to use for the wall tile given the surroundings
	 * @param s			The surrounding tiles as an int array
	 * @return		A String to tell what texture to use
	 */
	private static String setCorrectWall(int[][] s) {
		if (s[0][1] == 0) {			//Open below
			String use = "FRONT";
			if (s[1][2] == 0) use += "+R";
			if (s[1][0] == 0) use += "+L";
			return use;
		}
		else if (s[2][1] == 0) {	//Open above
			if (s[1][0] == 0 && s[1][2] == 0) return "BENT-U";
			if (s[1][2] == 0) return "BENT-R";
			if (s[1][0] == 0) return "BENT-L";
			return "SINGLE";
		}
		else if (s[1][0] == 0) {	//Open to the left
			//Rotate 90 degrees
			if (s[0][1] == 0 && s[2][1] == 0) return "BENT-U";
			if (s[2][1] == 0) return "BENT-R";
			if (s[1][1] == 0) return "BENT-L";
			return "SINGLE";
		}
		else if (s[1][2] == 0) {	//Open to the right
			//Rotate -90 degrees
			if (s[0][1] == 0 && s[2][1] == 0) return "BENT-U";
			if (s[1][1] == 0) return "BENT-R";
			if (s[2][1] == 0) return "BENT-L";
			return "SINGLE";
		}
		else {
			if (s[2][0] == 0) return "CORNER";
			if (s[2][2] == 0) return "CORNER";	//Rotate -90
			if (s[0][0] == 0) return "CORNER_FRONT-L";
			if (s[0][2] == 0) return "CORNER_FRONT-R";
		}
		return "NONE";
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
}
