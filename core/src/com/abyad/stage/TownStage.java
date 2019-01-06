package com.abyad.stage;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.CharacterHouse;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.EnterDungeonTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.actor.tile.WallTile;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.mapdata.MapEnvironment;

public class TownStage extends PlayStage{

	private static int[][] townMap = new int[19][19];
	private static int[][] roadMap = new int[townMap.length][townMap[0].length];
	private static int centerSquareRadius = 4;
	private static int doorRow = townMap.length - 1;
	private static int doorCol = townMap[0].length / 2;
	
	private static int spawnRow = townMap.length / 2;
	private static int spawnCol = townMap[0].length / 2;
	
	private static AbstractTile[][] townTileMap = new AbstractTile[townMap.length][townMap[0].length];
	private static EnterDungeonTile doorTile;
	
	private static CharacterHouse house;
	private static int houseRow = spawnRow + 3;
	private static int houseCol = spawnCol - centerSquareRadius;
	
	private boolean characterMenuFlag;
	
	static {
		for (int row = 0; row < townMap.length; row++) {
			townMap[row][0] = 1;
			townMap[row][townMap[row].length - 1] = 1;
		}
		for (int col = 0; col < townMap[0].length; col++) {
			townMap[0][col] = 1;
			townMap[townMap.length - 1][col] = 1;
		}
		int centerRow = townMap.length / 2;
		int centerCol = townMap[centerRow].length / 2;
		for (int row = centerRow - centerSquareRadius; row <= centerRow + centerSquareRadius; row++) {
			roadMap[row][centerCol - centerSquareRadius] = 1;
			roadMap[row][centerCol + centerSquareRadius] = 1;
		}
		for (int col = centerCol - centerSquareRadius; col <= centerCol + centerSquareRadius; col++) {
			roadMap[centerRow - centerSquareRadius][col] = 1;
			roadMap[centerRow + centerSquareRadius][col] = 1;
		}
		for (int row = doorRow; row >= centerRow + centerSquareRadius; row--) {
			roadMap[row][doorCol] = 1;
		}
		
		MapEnvironment townEnv = MapEnvironment.townEnvironment;
		for (int row = 0; row < townMap.length; row++) {
			for (int col = 0; col < townMap[row].length; col++) {
					if (townMap[row][col] == 0) {
						FloorTile floor = townEnv.getFloorTile(townMap, row, col);
						townTileMap[row][col] = floor;
					}
					if (townMap[row][col] == 1) {
						WallTile wall = townEnv.getWallTile(townMap, row, col);
						townTileMap[row][col] = wall;
					}
			}
		}
		townEnv.addRoads(townTileMap, roadMap);
		doorTile = new EnterDungeonTile(townEnv.getLockedStairsTexture(), townEnv.getUnlockedStairsTexture(),
				doorRow, doorCol, 0.0f, true);
		doorTile.interactables.remove(doorTile);
		townTileMap[doorRow][doorCol] = doorTile;
		house = new CharacterHouse((FloorTile)townTileMap[houseRow][houseCol]);
	}
	
	public TownStage(AbyssAdventureGame game) {
		super(game, false);
		
		//Generates the town part
		map = townMap;
		tileMap = townTileMap;
		for (AbstractTile[] tileRow : tileMap) {
			for (AbstractTile tile : tileRow) {
				addActor(tile);
			}
		}
		doorTile.interactables.add(doorTile);
		
		//Spawn in the players
		for (PlayerCharacter player : PlayerCharacter.getPlayers()) {
			player.setPosition(tileMap[spawnRow][spawnCol].getCenter().x, tileMap[spawnRow][spawnCol].getCenter().y);
			player.getVelocity().setLength(0);
			player.removeHeldItem();
			addActor(player);
		}
		addActor(house);
		house.initialize();
	}
	
	public void flagCharacterMenu(boolean flag) {
		characterMenuFlag = flag;
	}
	
	public boolean openCharacterMenu() {
		return characterMenuFlag;
	}

}
