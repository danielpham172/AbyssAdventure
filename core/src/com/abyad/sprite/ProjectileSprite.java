package com.abyad.sprite;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ProjectileSprite extends AnimationSprite{

	private static String[] rowNames = {"f", "r", "l", "b"};
	
	private int rows;
	private int cols;
	private int frameLengths;
	
	private boolean rotates;
	
	public ProjectileSprite(Texture sheet, int rows, int cols, int frameLengths) {
		this(sheet, rows, cols, frameLengths, false);
	}
	
	public ProjectileSprite(Texture sheet, int rows, int cols, int frameLengths, boolean rotates) {
		super();
		
		this.rotates = rotates;
		if (rotates) {
			this.rows = rows;
			this.cols = cols;
			this.frameLengths = frameLengths;
			int count = 0;
			TextureRegion[][] textureRegions = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					sprites.put("projectile_" + count, textureRegions[r][c]);
					count++;
				}
			}
		}
		else {
			this.rows = rows;
			this.cols = cols;
			this.frameLengths = frameLengths;
			TextureRegion[][] textureRegions = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					sprites.put("projectile_" + rowNames[r] + "_" + c, textureRegions[r][c]);
				}
			}
		}
	}
	
	@Override
	public ArrayList<TextureRegion> getNextFrame(String state, Vector2 direction, int framesSinceLast) {
		ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
		if (!rotates) {
			String dir = getDirection(direction);
			int frame = (framesSinceLast / frameLengths) % cols;
			frames.add(getSprite("projectile_" + dir + "_" + frame));
		}
		else {
			int frame = (framesSinceLast / frameLengths) % (cols * rows);
			frames.add(getSprite("projectile_" + frame));
		}
		return frames;
	}
	
	/**
	 * Gets the direction as a String. Locks in 4 direction to get the right sprite
	 * @param direction		The vector direction
	 * @return		The String character direction
	 */
	protected String getDirection(Vector2 direction) {
		String[] directions = {"r", "b", "l", "f"};
		return directions[((int)(direction.angle() + 45) / 90) % 4];
	}

}
