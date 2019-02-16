package com.abyad.sprite;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BarSprite extends AbstractSpriteSheet{

	private int widthSpacing;
	private int cols;
	
	public BarSprite(Texture sheet, int cols, int widthSpacing) {
		super();
		
		this.widthSpacing = widthSpacing;
		this.cols = cols;
		
		TextureRegion[][] textureRegions = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight());
		for (int c = 0; c < cols; c++) {
			sprites.put("bar_" + c, textureRegions[0][c]);
		}
	}
	
	public ArrayList<TextureRegion> getBarTextures(int amount, int partialAmount, int maxAmount){
		ArrayList<TextureRegion> textures = new ArrayList<TextureRegion>();
		for (int i = 0; i < Math.min(amount, maxAmount); i++) {
			textures.add(getSprite("bar_" + (cols - 1)));
		}
		
		if (partialAmount != 0 && textures.size() < maxAmount) {
			textures.add(getSprite("bar_" + (partialAmount)));
		}
		
		for (int i = textures.size(); i < maxAmount; i++) {
			textures.add(getSprite("bar_0"));
		}
		
		return textures;
	}
	
	public int getWidthSpacing() {
		return widthSpacing;
	}
}
