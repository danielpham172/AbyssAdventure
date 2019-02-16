package com.abyad.stage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.AbstractHouse;
import com.abyad.actor.mapobjects.CharacterHouse;
import com.abyad.actor.mapobjects.MagicHouse;
import com.abyad.actor.mapobjects.WeaponHouse;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.EnterDungeonTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.actor.tile.WallTile;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.game.Player;
import com.abyad.interfaces.Interactable;
import com.abyad.mapdata.MapEnvironment;

public class TownStage extends PlayStage{

	private static int[][] townMap = new int[19][19];
	private static int[][] roadMap = new int[townMap.length][townMap[0].length];
	private static int centerSquareRadius = 3;
	private static int outerSquareRadius = 7;
	private static int doorRow = townMap.length - 1;
	private static int doorCol = townMap[0].length / 2;
	
	private static int spawnRow = townMap.length / 2;
	private static int spawnCol = townMap[0].length / 2;
	
	private static AbstractTile[][] townTileMap = new AbstractTile[townMap.length][townMap[0].length];
	private static EnterDungeonTile doorTile;
	
	private static CharacterHouse house;
	private static int houseRow = spawnRow + 1;
	private static int houseCol = spawnCol - 5;
	
	private static WeaponHouse blacksmith;
	private static int blacksmithRow = spawnRow + 1;
	private static int blacksmithCol = spawnCol + 5;
	
	private static MagicHouse magicShop;
	private static int magicShopRow = spawnRow + centerSquareRadius + 1;
	private static int magicShopCol = spawnCol + 2;
	
	private boolean characterMenuFlag;
	private boolean weaponMenuFlag;
	private boolean magicMenuFlag;
	
	static {
		int centerRow = townMap.length / 2;
		int centerCol = townMap[centerRow].length / 2;
		createSquare(centerRow, centerCol, centerCol, townMap);
		createSquare(centerRow, centerCol, centerSquareRadius, roadMap);
		createSquare(centerRow, centerCol, outerSquareRadius, roadMap);
		for (int row = doorRow; row >= centerRow + centerSquareRadius; row--) {
			roadMap[row][doorCol] = 1;
		}
		for (int row = 2; row <= centerRow - centerSquareRadius; row++) {
			roadMap[row][doorCol] = 1;
		}
		//Connect Character House
		connectHouse(houseRow, houseCol, roadMap);
		//Connect Weapon House
		connectHouse(blacksmithRow, blacksmithCol, roadMap);
		//Connect Magic House
		connectHouse(magicShopRow, magicShopCol, roadMap);
		
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
		Interactable.interactables.remove(doorTile);
		townTileMap[doorRow][doorCol] = doorTile;
		house = new CharacterHouse((FloorTile)townTileMap[houseRow][houseCol]);
		blacksmith = new WeaponHouse((FloorTile)townTileMap[blacksmithRow][blacksmithCol]);
		magicShop = new MagicHouse((FloorTile)townTileMap[magicShopRow][magicShopCol]);
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
		for (Player player : game.getPlayers()) {
				PlayerCharacter character = player.getCharacter();
				character.setPosition(tileMap[spawnRow][spawnCol].getCenter().x, tileMap[spawnRow][spawnCol].getCenter().y);
				character.getVelocity().setLength(1.0f);
				character.removeHeldItem();
			if (player.isActive()) {
				addActor(character);
			}
		}
		addActor(house);
		addActor(blacksmith);
		addActor(magicShop);
		house.initialize();
		blacksmith.initialize();
		magicShop.initialize();
	}
	
	private static void connectHouse(int houseRow, int houseCol, int[][] map) {
		map[houseRow][houseCol] = 1;
		map[houseRow - 1][houseCol] = 1;
		int tempCol = houseCol;
		while(map[houseRow - 1][tempCol - 1] != 1) {
			tempCol--;
			map[houseRow - 1][tempCol] = 1;
		}
		tempCol = houseCol;
		while(map[houseRow - 1][tempCol + 1] != 1) {
			tempCol++;
			map[houseRow - 1][tempCol] = 1;
		}
	}
	
	private static void createSquare(int centerRow, int centerCol, int squareRadius, int[][] map) {
		for (int row = centerRow - squareRadius; row <= centerRow + squareRadius; row++) {
			map[row][centerCol - squareRadius] = 1;
			map[row][centerCol + squareRadius] = 1;
		}
		for (int col = centerCol - squareRadius; col <= centerCol + squareRadius; col++) {
			map[centerRow - squareRadius][col] = 1;
			map[centerRow + squareRadius][col] = 1;
		}
	}
	
	public void flagCharacterMenu(boolean flag) {
		characterMenuFlag = flag;
	}
	
	public boolean openCharacterMenu() {
		return characterMenuFlag;
	}

	public void flagWeaponMenu(boolean flag) {
		weaponMenuFlag = flag;
	}
	
	public boolean openWeaponMenu() {
		return weaponMenuFlag;
	}
	
	public void flagMagicMenu(boolean flag) {
		magicMenuFlag = flag;
	}
	
	public boolean openMagicMenu() {
		return magicMenuFlag;
	}

}
