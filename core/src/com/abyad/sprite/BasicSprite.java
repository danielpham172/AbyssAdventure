package com.abyad.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BasicSprite extends AbstractSpriteSheet{

	
	public BasicSprite(Texture sheet, int rows, int cols) {
		super();
		
		TextureRegion[][] textureRegions = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				sprites.put("r" + r + "_c" + c, textureRegions[r][c]);
			}
		}
	}
	
	public BasicSprite(Texture sheet, int rows, int cols, String[][] names) {
		super();
		
		TextureRegion[][] textureRegions = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				sprites.put(names[r][c], textureRegions[r][c]);
			}
		}
	}
}
