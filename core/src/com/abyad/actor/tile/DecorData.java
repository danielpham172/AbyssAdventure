package com.abyad.actor.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DecorData {

	private float xOffset;
	private float yOffset;
	private float rotation;
	private TextureRegion tex;
	
	public DecorData(TextureRegion tex) {
		this.setTex(tex);
	}
	
	public DecorData(TextureRegion tex, float rotation) {
		this.setTex(tex);
		this.setRotation(rotation);
	}

	public float getxOffset() {
		return xOffset;
	}

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public TextureRegion getTex() {
		return tex;
	}

	public void setTex(TextureRegion tex) {
		this.tex = tex;
	}
}
