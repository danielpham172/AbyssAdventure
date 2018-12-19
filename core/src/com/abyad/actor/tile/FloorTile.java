package com.abyad.actor.tile;

import java.util.ArrayList;

import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The FloorTile class is derived from the AbstractTile class and is used to draw the floors of the map
 *
 */
public class FloorTile extends AbstractTile{
	public static ArrayList<TextureRegion> floorTextures = new ArrayList<TextureRegion>();		//List of floor tiles.
	
	static {
		//Fancy work to splice the floor image into textures
		Texture floor = Assets.manager.get(Assets.floorTiles);
		TextureRegion[][] floorTiles = TextureRegion.split(floor, floor.getWidth() / 2, floor.getHeight() / 2);
		for (int r = 0; r < 2; r++) {
			for (int c = 0; c < 2; c++) {
				floorTextures.add(floorTiles[r][c]);
			}
		}
		
		floorTextures.remove(0);	//This is here since the first texture is actually just a base
	}
	
	/**
	 * Constructs a FloorTile at a row and column. Randomly selects a texture and rotates it randomly
	 * @param row		Row of the tile
	 * @param col		Column of the tile
	 */
	public FloorTile(int row, int col) {
		super(floorTextures.get((int)(Math.random() * floorTextures.size())), row, col, (int)(Math.random() * 4) * 90f);
	}
}
