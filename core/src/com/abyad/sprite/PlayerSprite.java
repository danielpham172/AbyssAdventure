package com.abyad.sprite;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PlayerSprite extends EntitySprite{

	private static String[] carryingColNames = {"carrying-idle", "carrying-rstep", "carrying-mstep", "carrying-lstep"};
	private static String[] weaponColNames = {"start", "swing", "follow", "end"};			//The column names for the weapon
	private static String[] rowNames = {"f", "r", "l", "b"};						//The row names for the weapon
	private static int[] attackLengths = {6, 10, 14, 24};				//The frame thresholds for the
	public static LinkedHashMap<String, TextureRegion> swordSprites = new LinkedHashMap<String, TextureRegion>();	//The hashmap for the sword sprites
	static {
		//Splicing and adding sword sprites
		Texture sword = Assets.manager.get(Assets.sword);
		TextureRegion[][] weaponRegions = TextureRegion.split(sword, sword.getWidth() / 4, sword.getHeight() / 4);
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				swordSprites.put("weapon_" + rowNames[r] + "_" + weaponColNames[c], weaponRegions[r][c]);
			}
		}
	}
	
	private static final int SHEET_ROWS = 4;
	private static final int SHEET_COLS = 12;
	
	/**
	 * Constructor for creating the sprite for a player.
	 * @param spriteSheet		The texture to use
	 */
	public PlayerSprite(Texture spriteSheet) {
		super(spriteSheet, SHEET_ROWS, SHEET_COLS);
		
		TextureRegion[][] charRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / SHEET_COLS, spriteSheet.getHeight() / SHEET_ROWS);
		for (int r = 0; r < 4; r++) {
			for (int c = 4; c < 8; c++) {
				sprites.put("char_" + rowNames[r] + "_" + carryingColNames[c - 4], charRegions[r][c]);
			}
		}
		for (int r = 0; r < 4; r++) {
			for (int c = 8; c < 12; c++) {
				sprites.put("char_" + rowNames[r] + "_" + weaponColNames[c - 8], charRegions[r][c]);
			}
		}
	}
	
	/**
	 * Gets the next frame to draw given the state, direction, and frame count of the vectors
	 */
	@Override
	public ArrayList<TextureRegion> getNextFrame(String state, Vector2 direction, int framesSinceLast) {
		if (state.equals("ATTACK - SWORD")) {
			ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
			int frame = 0;
			String dir = getDirection(direction);
			while (frame < 4 && framesSinceLast >= attackLengths[frame]) {
				frame++;
			}
			if (frame >= 4) frame = 3;
			TextureRegion weapon = swordSprites.get("weapon_" + dir + "_" + weaponColNames[frame]);
			TextureRegion character = sprites.get("char_" + dir + "_" + weaponColNames[frame]);
			if (isWeaponBehind(dir, frame)) {
				frames.add(weapon);
				frames.add(character);
			}
			else {
				frames.add(character);
				frames.add(weapon);
			}
			return frames;
		}
		else if (state.equals("CARRYING_IDLE")) {
			ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
			String dir = getDirection(direction);
			frames.add(getSprite("char_" + dir + "_carrying-idle"));
			return frames;
		}
		else if (state.equals("CARRYING_MOVE")) {
			ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
			String dir = getDirection(direction);
			int frame = (framesSinceLast / 6) % 4;
			String[] stepSelect = {"carrying-rstep", "carrying-mstep", "carrying-lstep", "carrying-mstep"};
			frames.add(getSprite("char_" + dir + "_" + stepSelect[frame]));
			return frames;
		}
		else if (state.equals("FALLING")) {
			ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
			String dir = getDirection(direction);
			frames.add(getSprite("char_" + dir + "_carrying-mstep"));
			return frames;
		}
		else {
			return super.getNextFrame(state, direction, framesSinceLast);
		}
	}
	
	/**
	 * Used to see whether to draw the weapon behind or in front
	 * @param dir
	 * @param frame
	 * @return
	 */
	private boolean isWeaponBehind (String dir, int frame) {
		if (dir.equals("b")) return true;
		else if (dir.equals("r") && frame >= 2) return true;
		else if (dir.equals("l") && frame <= 1) return true;
		else return false;
	}
}
