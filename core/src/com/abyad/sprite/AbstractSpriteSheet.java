package com.abyad.sprite;

import java.util.LinkedHashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AbstractSpriteSheet {
	protected LinkedHashMap<String, TextureRegion> sprites;
	
	public AbstractSpriteSheet() {
		sprites = new LinkedHashMap<String, TextureRegion>();
	}
	
	public TextureRegion getSprite(String name) {
		return sprites.get(name);
	}
}
