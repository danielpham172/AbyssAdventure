package com.abyad.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnvironmentSprite extends AbstractSpriteSheet{

	private static final String[] WALLS = {"SINGLE", "CORNER", "BENT-R", "BENT-L", "BENT-U",
			"FRONT", "FRONT+R", "FRONT+L", "FRONT+R+L", "CORNER_FRONT-R", "CORNER_FRONT-L", "NONE"};
	private int floorTileCount;
	private boolean allowFloorRotation;
	
	public EnvironmentSprite(Texture floor, int fRows, int fCols, Texture wall, int cRows, int cCols, Texture stair, boolean floorRotation) {
		super();
		
		TextureRegion[][] floorTiles = TextureRegion.split(floor, floor.getWidth() / fCols, floor.getHeight() / fRows);
		floorTileCount = fRows * fCols;
		allowFloorRotation = floorRotation;
		int count = 0;
		for (int r = 0; r < fRows; r++) {
			for (int c = 0; c < fCols; c++) {
				sprites.put("FLOOR_" + count, floorTiles[r][c]);
				count++;
			}
		}
		
		TextureRegion[][] wallTiles = TextureRegion.split(wall, wall.getWidth() / cCols, wall.getHeight() / cRows);
		count = 0;
		for (int r = 0; r < cRows; r++) {
			for (int c = 0; c < cCols; c++) {
				if (count != WALLS.length) {
					sprites.put(WALLS[count], wallTiles[r][c]);
					count++;
				}
			}
		}
		
		while (count < WALLS.length) {
			sprites.put(WALLS[count], null);
			count++;
		}
		
		TextureRegion[] stairTiles = TextureRegion.split(stair, stair.getWidth() / 2, stair.getHeight())[0];
		sprites.put("UNLOCKED_STAIRS", stairTiles[0]);
		sprites.put("LOCKED_STAIRS", stairTiles[1]);
	}
	
	
	public TextureRegion getRandomFloorSprite() {
		return sprites.get("FLOOR_" + (int)(Math.random() * floorTileCount));
	}
	
	public float getRandomRotation() {
		if (allowFloorRotation) {
			return (int)(Math.random() * 4) * 90f;
		}
		else return 0;
	}
	
	public TextureRegion getWallSprite(int[][] surroundings) {
		return sprites.get(getCorrectWall(surroundings));
	}
	
	/**
	 * Selects the texture to use for the wall tile given the surroundings
	 * @param s			The surrounding tiles as an int array
	 * @return		A String to tell what texture to use
	 */
	private static String getCorrectWall(int[][] s) {
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

}
