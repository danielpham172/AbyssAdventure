package com.abyad.sprite;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EntitySprite extends AbstractSpriteSheet{

	//Basic Entity Sprite Sheet, consisting of 4 directional movement and idle
	//Col 0 - 3: Idle, Right Step, Middle Step, Left Step
	//Row 0 - 3: Front, Right, Left, Back
	
	private static String[] colNames = {"idle", "rstep", "mstep", "lstep"};
	private static String[] rowNames = {"f", "r", "l", "b"};
	public EntitySprite(Texture spriteSheet) {
		super();
		
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 4, spriteSheet.getHeight() / 4);
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				sprites.put("char_" + rowNames[r] + "_" + colNames[c], textureRegions[r][c]);
			}
		}
	}
	
	public EntitySprite(Texture spriteSheet, int rows, int cols) {
		super();
		
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / cols, spriteSheet.getHeight() / rows);
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				sprites.put("char_" + rowNames[r] + "_" + colNames[c], textureRegions[r][c]);
			}
		}
	}
	
	public ArrayList<TextureRegion>  getNextFrame(String state, Vector2 direction, int framesSinceLast) {
		String dir = getDirection(direction);
		ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
		if (state.equals("IDLE")) {
			frames.add(getSprite("char_" + dir + "_idle"));
			return frames;
		}
		else {
			int frame = (framesSinceLast / 6) % 4;
			String[] stepSelect = {"rstep", "mstep", "lstep", "mstep"};
			frames.add(getSprite("char_" + dir + "_" + stepSelect[frame]));
			return frames;
		}
	}
	
	protected String getDirection(Vector2 direction) {
		String[] directions = {"r", "b", "l", "f"};
		return directions[((int)(direction.angle() + 45) / 90) % 4];
	}
}
