package com.abyad.mapdata;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileData {

	private TextureRegion texture;
	
	private String tileType;
	private float fixedRotation;
	private boolean isFixedRotation;
	private int choiceWeight;
	private boolean isFrontWall;
	
	public TileData() {
		choiceWeight = 1;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	public String getTileType() {
		return tileType;
	}

	public void setTileType(String tileType) {
		this.tileType = tileType;
	}

	public float getRotation() {
		if (isFixedRotation) {
			return fixedRotation;
		}
		else {
			return 90f * (int)(Math.random() * 4);
		}
	}

	public void setFixedRotation(float fixedRotation) {
		this.fixedRotation = fixedRotation;
	}

	public boolean isFixedRotation() {
		return isFixedRotation;
	}

	public void setIsFixedRotation(boolean isFixedRotation) {
		this.isFixedRotation = isFixedRotation;
	}

	public int getChoiceWeight() {
		return choiceWeight;
	}

	public void setChoiceWeight(int choiceWeight) {
		this.choiceWeight = choiceWeight;
	}

	public boolean isFrontWall() {
		return isFrontWall;
	}

	public void setFrontWall(boolean isFrontWall) {
		this.isFrontWall = isFrontWall;
	}
}
