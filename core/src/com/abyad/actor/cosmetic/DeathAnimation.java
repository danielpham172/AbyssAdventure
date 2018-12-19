package com.abyad.actor.cosmetic;

import java.util.ArrayList;

import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DeathAnimation extends Actor{
	private static ArrayList<TextureRegion> deathExpSprites = new ArrayList<TextureRegion>();
	private static final int SPRITE_LENGTH = 32;
	
	static {
		Texture spriteSheet = Assets.manager.get(Assets.deathAnim);
		int rows = 2;
		int cols = 3;
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / cols, spriteSheet.getHeight() / rows);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				deathExpSprites.add(textureRegions[r][c]);
			}
		}
	}
	
	private int frameCount = 0;
	private static int[] frameLengths = {6, 12, 18, 24, 30, 36};
	
	public DeathAnimation(float f, float g) {
		setPosition(f, g);
		setOrigin(SPRITE_LENGTH / 2, SPRITE_LENGTH / 2);
	}
	
	@Override
	public void act(float delta) {
		frameCount++;
		if (frameCount > frameLengths[frameLengths.length - 1]) remove();
	}
	
	@Override
	public void draw(Batch batch, float a) {
		int frame = 0;
		while (frame < frameLengths.length && frameCount > frameLengths[frame]) {
			frame++;
		}
		
		frame = Math.min(frame, deathExpSprites.size() - 1);
		TextureRegion deathSprite = deathExpSprites.get(frame);
		
		batch.draw(deathSprite, getX() - getOriginX(), getY() - getOriginY(), getOriginX(),
				getOriginY(), SPRITE_LENGTH, SPRITE_LENGTH, getScaleX(), getScaleY(), getRotation());
	}
}
