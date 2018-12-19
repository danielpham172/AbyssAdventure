package com.abyad.actor.tile;

import java.util.ArrayList;

import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FloorTile extends AbstractTile{
	public static ArrayList<TextureRegion> floorTextures = new ArrayList<TextureRegion>();
	
	static {
		Texture floor = Assets.manager.get(Assets.floorTiles);
		TextureRegion[][] floorTiles = TextureRegion.split(floor, floor.getWidth() / 2, floor.getHeight() / 2);
		for (int r = 0; r < 2; r++) {
			for (int c = 0; c < 2; c++) {
				floorTextures.add(floorTiles[r][c]);
			}
		}
		
		floorTextures.remove(0);
	}
	
	public FloorTile(int row, int col) {
		super(floorTextures.get((int)(Math.random() * floorTextures.size())), row, col, (int)(Math.random() * 4) * 90f);
	}
}
