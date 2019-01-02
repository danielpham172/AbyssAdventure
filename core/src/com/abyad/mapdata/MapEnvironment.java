package com.abyad.mapdata;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.actor.tile.StairTile;
import com.abyad.actor.tile.WallTile;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.utils.FileReads;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MapEnvironment {

	public static LinkedHashMap<String, MapEnvironment> environments = new LinkedHashMap<String, MapEnvironment>();
	public static MapEnvironment townEnvironment;
	
	private TileDataTree floorTiles = new TileDataTree(0);
	private TileDataTree wallTiles = new TileDataTree(0);
	
	private TileData unlockedStairs;
	private TileData lockedStairs;
	
	private String dungeonName;
	private String folderDirectory;
	
	private AbstractSpriteSheet sprites;
	
	public MapEnvironment(String dungeonName, String folderName) {
		this.dungeonName = dungeonName;
		folderDirectory = folderName;
	}

	public static void setupEnvironments() {
		for (String dungeonName : environments.keySet()) {
			environments.get(dungeonName).setupEnvironment();
		}
		townEnvironment.setupEnvironment();
	}
	
	private void setupEnvironment() {
		sprites = AbstractSpriteSheet.spriteSheets.get(dungeonName);
		for (String data : FileReads.readFileToArray("tile/" + folderDirectory + "/dungeonData.txt")) {
			addTileData(data);
		}
	}
	
	public FloorTile getFloorTile(int[][] dungeon, int row, int col) {
		int surroundings = getSurroundings(dungeon, row, col);
		ArrayList<TileData> tileDatas = floorTiles.getTileDatas(surroundings);
		int weightSum = 0;
		for (TileData tileData : tileDatas) {
			weightSum += tileData.getChoiceWeight();
		}
		
		int choice = (int)(Math.random() * weightSum);
		for (TileData tileData : tileDatas) {
			choice -= tileData.getChoiceWeight();
			if (choice < 0) {
				FloorTile tile = new FloorTile(tileData.getTexture(), row, col, tileData.getRotation());
				return tile;
			}
		}
		
		return new FloorTile(null, row, col, 0);
	}

	public WallTile getWallTile(int[][] dungeon, int row, int col) {
		int surroundings = getSurroundings(dungeon, row, col);
		ArrayList<TileData> tileDatas = wallTiles.getTileDatas(surroundings);
		int weightSum = 0;
		for (TileData tileData : tileDatas) {
			weightSum += tileData.getChoiceWeight();
		}
		
		int choice = (int)(Math.random() * weightSum);
		for (TileData tileData : tileDatas) {
			choice -= tileData.getChoiceWeight();
			if (choice < 0) {
				WallTile tile = new WallTile(tileData.getTexture(), row, col, tileData.getRotation(), tileData.isFrontWall());
				return tile;
			}
		}
		
		return new WallTile(null, row, col, 0, false);
	}
	
	public StairTile getStairs(int row, int col, boolean locked) {
		return new StairTile(row, col, locked, unlockedStairs.getTexture(), lockedStairs.getTexture());
	}
	
	public TextureRegion getUnlockedStairsTexture() {
		return unlockedStairs.getTexture();
	}
	public TextureRegion getLockedStairsTexture() {
		return lockedStairs.getTexture();
	}
	
	private int getSurroundings(int[][] dungeon, int row, int col){
		//int[][] surroundings = new int[3][3];
		int number = 0;
		int count = 0;
		for (int r = -1; r <= 1; r++) {
			for (int c = 1; c >= -1; c--) {
				if (r != 0 || c != 0) {
					if (inBounds(dungeon, row + r, col + c)) {
						number += (int)Math.pow(10, count) * dungeon[row + r][col + c];
					}
					else {
						number += (int)Math.pow(10, count);
					}
					count++;
				}
			}
		}
		
		return number;
	}
	
	private boolean inBounds(int[][] dungeon, int row, int col) {
		return (row >= 0 && row < dungeon.length && col >= 0 && col < dungeon[0].length);
	}
	
	private void addTileData(String strData) {
		int row = -1;
		int col = -1;
		int surroundings = -1;
		
		boolean isIndependent = false;
		boolean setUpFixedRotation = false;
		boolean setUpIndependent = false;
		
		TileData tileData = new TileData();
		String data = strData.substring(0);
		while (!data.isEmpty()) {
			int spaceIndex = data.indexOf(' ');
			String parameter;
			if (spaceIndex != -1) {
				parameter = data.substring(0, spaceIndex);
				data = data.substring(spaceIndex + 1);
			}
			else {
				parameter = data;
				data = "";
			}
			
			String type = getParameterType(parameter);
			int number = getParameterNumber(parameter);
			
			if (type != null && number != -1) {
				if (type.equals("ROW")) {
					//Row on the spritesheet that the tile is located
					row = number;
					if (row != -1 && col != -1) tileData.setTexture(sprites.getSprite("r" + row + "_c" + col));
				}
				else if (type.equals("COL")) {
					//Col on the spritesheet that the tile is located
					col = number;
					if (row != -1 && col != -1) tileData.setTexture(sprites.getSprite("r" + row + "_c" + col));
				}
				else if (type.equals("TYPE")) {
					//Type of tile (floor or wall)
					if (number == 0) tileData.setTileType("FLOOR");
					else if (number == 1) tileData.setTileType("WALL");
					else if (number == 2) tileData.setTileType("UNLOCKED_STAIRS");
					else if (number == 3) tileData.setTileType("LOCKED_STAIRS");
				}
				else if (type.equals("SURROUNDINGS_INDEX")) {
					//The kind of surroundings it has
					surroundings = number;
				}
				else if (type.equals("INDEPENDENT")) {
					//Whether the tile is not locked to a surroundings type
					//Floors default to True, Walls default to False
					isIndependent = isTrueNumber(number);
					
					setUpIndependent = true;
				}
				else if (type.equals("FIXED_ROTATION")) {
					//If the tile has a fixed rotation.
					//Floors default to False, Walls default to True
					tileData.setIsFixedRotation(isTrueNumber(number));
					
					setUpFixedRotation = true;
				}
				else if (type.equals("ROTATE")) {
					//The rotation of the tile. Only applies if the tile has fixed rotation on.
					tileData.setFixedRotation(number);
				}
				else if (type.equals("SELECTION_WEIGHT")) {
					//The weight of the selection of the tile
					//Defaults to 1 for all tiles
					tileData.setChoiceWeight(number);
				}
				else if (type.equals("FRONT_WALL")) {
					//Sets if the tile is a front wall. Defaults to false
					tileData.setFrontWall(isTrueNumber(number));
				}
			}
		}
		
		//Successfully generated a tile data
		if (tileData.getTexture() != null && tileData.getTileType() != null &&
				((surroundings != -1) || (isIndependent) ||
						((tileData.getTileType().equals("FLOOR") || tileData.getTileType().contains("STAIRS")) && !setUpIndependent))) {
			if (tileData.getTileType().equals("FLOOR")) {
				//Some initial things if not setup yet
				if (!setUpIndependent) isIndependent = true;
				if (!setUpFixedRotation) tileData.setIsFixedRotation(false);
				
				if (isIndependent) surroundings = 22222222;
				floorTiles.addTileData(tileData, surroundings);
			}
			else if (tileData.getTileType().equals("WALL")) {
				//Some initial things if not setup yet
				if (!setUpIndependent) isIndependent = false;
				if (!setUpFixedRotation) tileData.setIsFixedRotation(true);
				
				if (isIndependent) surroundings = 22222222;
				wallTiles.addTileData(tileData, surroundings);
			}
			else if (tileData.getTileType().equals("UNLOCKED_STAIRS")) {
				if (!setUpIndependent) isIndependent = false;
				if (!setUpFixedRotation) tileData.setIsFixedRotation(true);
				
				unlockedStairs = tileData;
			}
			else if (tileData.getTileType().equals("LOCKED_STAIRS")){
				if (!setUpIndependent) isIndependent = false;
				if (!setUpFixedRotation) tileData.setIsFixedRotation(true);
				
				lockedStairs = tileData;
			}
		}
	}
	
	private String getParameterType(String str) {
		int colonIndex = str.indexOf(':');
		if (colonIndex != -1) return str.substring(0, colonIndex); else return null;
	}
	
	private int getParameterNumber(String str) {
		int colonIndex = str.indexOf(':');
		if (colonIndex != -1) {
			try {
				return Integer.parseInt(str.substring(colonIndex + 1));
			}
			catch (NumberFormatException e) {
				return -1;
			}
		}
		else return -1;
	}
	
	private boolean isTrueNumber(int number) {
		return (number % 2 == 1);
	}

	public String getFileDirectory() {
		return folderDirectory;
	}
}
