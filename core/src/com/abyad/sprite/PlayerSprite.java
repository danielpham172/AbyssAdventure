package com.abyad.sprite;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PlayerSprite extends EntitySprite{

	private static String[] carryingColNames = {"carrying-idle", "carrying-rstep", "carrying-mstep", "carrying-lstep"};
	
	private static String[] weaponColNames = {"start", "swing", "follow", "end", "start-swing", "end-swing"};			//The column names for the weapon
	private static String[] rowNames = {"f", "r", "l", "b"};															//The row names for the weapon
	
	private static String[] staffColNames = {"start", "swing", "follow", "end", "low-hold", "high-hold"};
	private static String[] spearColNames = {"start-stab", "stab", "follow-stab", "end-stab"};
	
	public static LinkedHashMap<String, TextureRegion> swordSprites = new LinkedHashMap<String, TextureRegion>();	//The hashmap for the sword sprites
	public static LinkedHashMap<String, TextureRegion> staffSprites = new LinkedHashMap<String, TextureRegion>();	//The hashmap for the staff sprites
	public static LinkedHashMap<String, TextureRegion> spearSprites = new LinkedHashMap<String, TextureRegion>();	//The hashmap for the spear sprites
	static {
		//Splicing and adding sword sprites
		int swordRows = 4;
		int swordCols = 6;
		Texture sword = Assets.manager.get(Assets.sword);
		TextureRegion[][] swordRegions = TextureRegion.split(sword, sword.getWidth() / swordCols, sword.getHeight() / swordRows);
		for (int r = 0; r < swordRows; r++) {
			for (int c = 0; c < swordCols; c++) {
				swordSprites.put("weapon_" + rowNames[r] + "_" + weaponColNames[c], swordRegions[r][c]);
			}
		}
		
		int staffRows = 4;
		int staffCols = 6;
		Texture staff = Assets.manager.get(Assets.staff);
		TextureRegion[][] staffRegions = TextureRegion.split(staff, staff.getWidth() / staffCols, staff.getHeight() / staffRows);
		for (int r = 0; r < staffRows; r++) {
			for (int c = 0; c < staffCols; c++) {
				staffSprites.put("weapon_" + rowNames[r] + "_" + staffColNames[c], staffRegions[r][c]);
			}
		}
		
		//Splicing and adding spear sprites
		int spearRows = 4;
		int spearCols = 4;
		Texture spear = Assets.manager.get(Assets.spear);
		TextureRegion[][] spearRegions = TextureRegion.split(spear, spear.getWidth() / spearCols, spear.getHeight() / spearRows);
		for (int r = 0; r < spearRows; r++) {
			for (int c = 0; c < spearCols; c++) {
				spearSprites.put("weapon_" + rowNames[r] + "_" + spearColNames[c], spearRegions[r][c]);
			}
		}
	}
	
	private static final int SHEET_ROWS = 4;
	private static final int SHEET_COLS = 19;
	
	/**
	 * Constructor for creating the sprite for a player.
	 * @param spriteSheet		The texture to use
	 */
	public PlayerSprite(Texture spriteSheet, Texture icons) {
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
		for (int r = 0; r < 4; r++) {
			for (int c = 12; c < 16; c++) {
				sprites.put("char_" + rowNames[r] + "_" + spearColNames[c - 12], charRegions[r][c]);
			}
		}
		for (int r = 0; r < 4; r++) {
			for (int c = 16; c < 18; c++) {
				sprites.put("char_" + rowNames[r] + "_" + staffColNames[c - 16], charRegions[r][c]);
			}
		}
		for (int r = 0; r < 4; r++) {
			sprites.put("char_" + rowNames[r] + "_dead", charRegions[r][18]);
		}
		TextureRegion[][] iconRegions = TextureRegion.split(icons, icons.getWidth() / 2, icons.getHeight());
		sprites.put("head", iconRegions[0][0]);
		sprites.put("magic_cursor", iconRegions[0][1]);
	}
	
	/**
	 * Gets the next frame to draw given the state, direction, and frame count of the vectors
	 */
	@Override
	public ArrayList<TextureRegion> getNextFrame(String state, Vector2 direction, int framesSinceLast) {
		if (state.contains("BASIC_ATTACK")) {
			return getNextFrameBasicAttack(state, direction, framesSinceLast);
		}
		else if (state.contains("SPECIAL_ATTACK")) {
			return getNextFrameSpecialAttack(state, direction, framesSinceLast);
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
		else if (state.equals("CASTING")) {
			ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
			String dir = getDirection(direction);
			int frame = (framesSinceLast / 5) % 5;
			if (frame >= 3) {
				frames.add(getSprite("char_" + dir + "_carrying-mstep"));
			}
			else {
				frames.add(getSprite("char_" + dir + "_idle"));
			}
			return frames;
		}
		else if (state.equals("FINISH_CASTING")) {
			ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
			String dir = getDirection(direction);
			frames.add(getSprite("char_" + dir + "_swing"));
			return frames;
		}
		else {
			return super.getNextFrame(state, direction, framesSinceLast);
		}
	}
	
	public ArrayList<TextureRegion> getNextFrameBasicAttack(String state, Vector2 direction, int framesSinceLast){
		ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
		if (state.contains("SWORD")) {
			int frame = 0;
			int[] attackLengths = {6, 10, 14, 24};				//The frame thresholds for the attack
			String dir = getDirection(direction);
			while (frame < 4 && framesSinceLast >= attackLengths[frame]) {
				frame++;
			}
			if (frame >= 4) frame = 3;
			TextureRegion weapon = swordSprites.get("weapon_" + dir + "_" + weaponColNames[frame]);
			TextureRegion character = sprites.get("char_" + dir + "_" + weaponColNames[frame]);
			if (isWeaponBehind(dir, frame, "SWORD")) {
				frames.add(weapon);
				frames.add(character);
			}
			else {
				frames.add(character);
				frames.add(weapon);
			}
		}
		else if (state.contains("STAFF")) {
			int frame = 0;
			int[] attackLengths = {10, 16, 22, 34};				//The frame thresholds for the attack
			String dir = getDirection(direction);
			while (frame < 4 && framesSinceLast >= attackLengths[frame]) {
				frame++;
			}
			if (frame >= 4) frame = 3;
			TextureRegion weapon = staffSprites.get("weapon_" + dir + "_" + weaponColNames[frame]);
			TextureRegion character = sprites.get("char_" + dir + "_" + weaponColNames[frame]);
			if (isWeaponBehind(dir, frame, "STAFF")) {
				frames.add(weapon);
				frames.add(character);
			}
			else {
				frames.add(character);
				frames.add(weapon);
			}
		}
		else if (state.contains("SPEAR")) {
			int frame = 0;
			int[] attackLengths = {7, 15, 20, 24};				//The frame thresholds for the attack
			String dir = getDirection(direction);
			while (frame < 4 && framesSinceLast >= attackLengths[frame]) {
				frame++;
			}
			if (frame >= 4) frame = 3;
			TextureRegion weapon = spearSprites.get("weapon_" + dir + "_" + spearColNames[frame]);
			TextureRegion character = sprites.get("char_" + dir + "_" + spearColNames[frame]);
			if (isWeaponBehind(dir, frame, "SPEAR")) {
				frames.add(weapon);
				frames.add(character);
			}
			else {
				frames.add(character);
				frames.add(weapon);
			}
		}
		return frames;
	}
	
	public ArrayList<TextureRegion> getNextFrameSpecialAttack(String state, Vector2 direction, int framesSinceLast){
		ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
		if (state.contains("SPIN_SLASH")) {
			int frame = 0;
			int[] attackLengths = {4, 10, 16, 22, 28, 34};				//The frame thresholds for the attack
			while (frame < 6 && framesSinceLast >= attackLengths[frame]) {
				frame++;
			}
			if (frame >= 6) frame = 5;
			Vector2 newDirection = new Vector2(direction);
			if (frame != 5) newDirection.rotate(frame * 90f);
			String dir = getDirection(newDirection);
			
			TextureRegion weapon;
			TextureRegion character;
			if (frame == 0) {
				weapon = swordSprites.get("weapon_" + dir + "_start");
				character = sprites.get("char_" + dir + "_start");
			}
			else if (frame == 5) {
				weapon = swordSprites.get("weapon_" + dir + "_" + weaponColNames[4]);
				character = sprites.get("char_" + dir + "_swing");
			}
			else {
				weapon = swordSprites.get("weapon_" + dir + "_" + weaponColNames[5]);
				character = sprites.get("char_" + dir + "_swing");
			}
			
			if (isWeaponBehind(dir, frame, "SPIN_SLASH")) {
				frames.add(weapon);
				frames.add(character);
			}
			else {
				frames.add(character);
				frames.add(weapon);
			}
		}
		if (state.contains("WIND_BLADE")) {
			int frame = 0;
			int[] attackLengths = {6, 10, 14, 24};				//The frame thresholds for the attack
			String dir = getDirection(direction);
			while (frame < 4 && framesSinceLast >= attackLengths[frame]) {
				frame++;
			}
			if (frame >= 4) frame = 3;
			TextureRegion weapon = swordSprites.get("weapon_" + dir + "_" + weaponColNames[frame]);
			TextureRegion character = sprites.get("char_" + dir + "_" + weaponColNames[frame]);
			if (isWeaponBehind(dir, frame, "SWORD")) {
				frames.add(weapon);
				frames.add(character);
			}
			else {
				frames.add(character);
				frames.add(weapon);
			}
		}
		if (state.contains("MEDITATE")) {
			int frame = 0;
			int[] attackLengths = {30, 40};				//The frame thresholds for the attack
			String dir = getDirection(direction);
			while (frame < 2 && framesSinceLast >= attackLengths[frame]) {
				frame++;
			}
			if (frame >= 2) frame = 1;
			TextureRegion weapon = staffSprites.get("weapon_" + dir + "_" + staffColNames[frame + 4]);
			TextureRegion character = sprites.get("char_" + dir + "_" + staffColNames[frame + 4]);
			if (isWeaponBehind(dir, frame, "STAFF")) {
				frames.add(weapon);
				frames.add(character);
			}
			else {
				frames.add(character);
				frames.add(weapon);
			}
		}
		if (state.contains("PIERCE_CHARGE")) {
			int frame = 0;
			int[] attackLengths = {30, 120, 140, 150};				//The frame thresholds for the attack
			String dir = getDirection(direction);
			while (frame < 4 && framesSinceLast >= attackLengths[frame]) {
				frame++;
			}
			if (frame >= 4) frame = 3;
			TextureRegion weapon = spearSprites.get("weapon_" + dir + "_" + spearColNames[frame]);
			TextureRegion character = sprites.get("char_" + dir + "_" + spearColNames[frame]);
			if (isWeaponBehind(dir, frame, "SPEAR")) {
				frames.add(weapon);
				frames.add(character);
			}
			else {
				frames.add(character);
				frames.add(weapon);
			}
		}
		return frames;
	}
	
	/**
	 * Used to see whether to draw the weapon behind or in front
	 * @param dir
	 * @param frame
	 * @return
	 */
	private boolean isWeaponBehind (String dir, int frame, String type) {
		if (type.equals("SWORD")) {
			if (dir.equals("b")) return true;
			else if (dir.equals("r") && frame >= 2) return true;
			else if (dir.equals("l") && frame <= 1) return true;
		}
		else if (type.equals("STAFF")) {
			if (dir.equals("b")) return true;
			else if (dir.equals("r") && frame >= 2) return true;
			else if (dir.equals("l") && frame <= 1) return true;
		}
		else if (type.equals("SPEAR")) {
			if (dir.equals("b") || dir.equals("l")) return true;
		}
		else if (type.equals("SPIN_SLASH")) {
			if (dir.equals("b") || dir.equals("l")) return true;
		}
		return false;
	}
}
