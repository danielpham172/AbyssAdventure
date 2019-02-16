package com.abyad.sprite;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.abyad.utils.Assets;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MagicSprite extends AnimationSprite{
	
	private int circleFrames;
	private int circleFrameLengths;
	private int particleFrames;
	private int particleFrameLengths;
	
	public MagicSprite(LinkedHashMap<String, AssetDescriptor<Texture>> textures, int circleFrames, int circleFrameLengths, int particleFrames, int particleFrameLengths) {
		this.circleFrames = circleFrames;
		this.circleFrameLengths = circleFrameLengths;
		this.particleFrames = particleFrames;
		this.particleFrameLengths = particleFrameLengths;
		
		sprites.put("ICON", TextureRegion.split(Assets.manager.get(textures.get("ICON")),
				Assets.manager.get(textures.get("ICON")).getWidth(), Assets.manager.get(textures.get("ICON")).getHeight())[0][0]);
		if (textures.containsKey("CIRCLE")) {
			Texture circle = Assets.manager.get(textures.get("CIRCLE"));
			TextureRegion[] textureRegions = TextureRegion.split(circle, circle.getWidth() / circleFrames, circle.getHeight())[0];
			for (int c = 0; c < circleFrames; c++) {
				sprites.put("circle_" + c, textureRegions[c]);
			}
		}
		if (textures.containsKey("PARTICLE")) {
			Texture particle = Assets.manager.get(textures.get("PARTICLE"));
			TextureRegion[] textureRegions = TextureRegion.split(particle, particle.getWidth() / particleFrames, particle.getHeight())[0];
			for (int c = 0; c < particleFrames; c++) {
				sprites.put("particle_" + c, textureRegions[c]);
			}
		}
	}
	
	@Override
	public ArrayList<TextureRegion> getNextFrame(String state, Vector2 direction, int framesSinceLast) {
		ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
		if (state.equals("CIRCLE")) {
			int frame = (framesSinceLast / circleFrameLengths) % circleFrames;
			frames.add(getSprite("circle_" + frame));
		}
		if (state.equals("PARTICLE")) {
			int frame = (framesSinceLast / particleFrameLengths) % particleFrames;
			frames.add(getSprite("particle_" + frame));
		}
		return frames;
	}

}
