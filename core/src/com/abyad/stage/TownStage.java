package com.abyad.stage;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.EnterDungeonTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.actor.tile.WallTile;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.mapdata.MapEnvironment;

public class TownStage extends PlayStage{

	private static int[][] townMap = new int[19][19];
	private static int doorRow = townMap.length - 1;
	private static int doorCol = townMap[0].length / 2;
	
	private static int spawnRow = townMap.length / 2;
	private static int spawnCol = townMap[0].length / 2;
	
	private static AbstractTile[][] townTileMap = new AbstractTile[townMap.length][townMap[0].length];
	private static EnterDungeonTile doorTile;
	
	static {
		for (int row = 0; row < townMap.length; row++) {
			townMap[row][0] = 1;
			townMap[row][townMap[row].length - 1] = 1;
		}
		for (int col = 0; col < townMap[0].length; col++) {
			townMap[0][col] = 1;
			townMap[townMap.length - 1][col] = 1;
		}
		
		MapEnvironment townEnv = MapEnvironment.townEnvironment;
		for (int row = 0; row < townMap.length; row++) {
			for (int col = 0; col < townMap[row].length; col++) {
				if (row == doorRow && col == doorCol) {
					doorTile = new EnterDungeonTile(townEnv.getLockedStairsTexture(), townEnv.getUnlockedStairsTexture(),
							row, col, 0.0f, true);
					doorTile.interactables.remove(doorTile);
					townTileMap[row][col] = doorTile;
				}
				else {
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
		}
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
	}

}
