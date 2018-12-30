package com.abyad.stage;

import java.util.ArrayList;
import java.util.Comparator;

import com.abyad.actor.cosmetic.DeathAnimation;
import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.entity.ZombieCharacter;
import com.abyad.actor.mapobjects.RareTreasureChest;
import com.abyad.actor.mapobjects.TreasureChest;
import com.abyad.actor.mapobjects.items.KeyItem;
import com.abyad.actor.mapobjects.items.LootItem;
import com.abyad.actor.mapobjects.items.MapItem;
import com.abyad.actor.projectile.OnGroundProjectile;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.actor.tile.StairTile;
import com.abyad.actor.tile.WallTile;
import com.abyad.actor.ui.MagicCursor;
import com.abyad.actor.ui.MagicRingMenu;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.EnvironmentSprite;
import com.abyad.utils.DungeonGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * The PlayStage is derived from the Stage class. This creates the map for the game.
 *
 */
public class PlayStage extends Stage{

	private AbyssAdventureGame game;				//The game object
	private int[][] map;							//The int array map
	private AbstractTile[][] tileMap;				//The map of the tiles
	private ArrayList<Rectangle> rooms;				//The rooms (x = row, y = col)
	private ArrayList<FloorTile> floorTiles;		//The floor tiles
	private ArrayList<FloorTile> roomTiles;			//The room tiles
	
	private ArrayList<TreasureChest> treasure;		//All treasure chests
	private ArrayList<AbstractEntity> enemies;		//All enemies
	
	private boolean readyForNextLevel;
	private EnvironmentSprite environment;
	
	//Comparator object to organize the actors
	private static ActorComparator comparator = new ActorComparator();
	
	private static String[] environments = {"MOSSY_DUNGEON"};
	
	/**
	 * Constructor to create the stage
	 * @param game			The game object
	 */
	public PlayStage(AbyssAdventureGame game) {
		super(new ExtendViewport(384, 216));		//Creates the stage with a viewport
		this.game = game;
		environment = (EnvironmentSprite)AbstractSpriteSheet.spriteSheets.get(
				environments[(int)(Math.random() * environments.length)]);
		
		DungeonGenerator generator = new DungeonGenerator(18, 18, 3, 5, 600);		//Create a generator
		generator.runDungeonGenerator();		//Generate the dungeon
		map = generator.getDungeon();			//Set the map
		tileMap = new AbstractTile[map.length][map[0].length];
		floorTiles = new ArrayList<FloorTile>();
		roomTiles = new ArrayList<FloorTile>();
		rooms = generator.getRooms();
		
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				if (map[r][c] == 0) {
					FloorTile tile = new FloorTile(r, c, environment);
					addActor(tile);
					tileMap[r][c] = tile;
					floorTiles.add(tile);
					if (isRoomTile(r, c)) {
						roomTiles.add(tile);
					}
				}
				if (map[r][c] == 1) {
					createWallTile(r, c);	//Used to see if a wall tile needs to be made
				}
			}
		}
		
		//Generate enemies
		enemies = new ArrayList<AbstractEntity>();
		for (int i = 0; i < 30; i++) {
			Vector2 randomPos = getRandomOpenPos();
			ZombieCharacter zombie = new ZombieCharacter(randomPos.x, randomPos.y);
			enemies.add(zombie);
			addActor(zombie);
		}
		
		//Generating treasure
		ArrayList<Rectangle> randomRooms = new ArrayList<Rectangle>(rooms);
		treasure = new ArrayList<TreasureChest>();
		while (!randomRooms.isEmpty()) {
			Rectangle room = randomRooms.remove((int)(Math.random() * randomRooms.size()));
			if (Math.random() < 1) {
				if (Math.random() < 0.1 && (!enemies.isEmpty() || !treasure.isEmpty())) {
					//Spawn rare treasure
					FloorTile center = (FloorTile)tileMap[(int)(room.getX() + room.getWidth() / 2)][(int)(room.getY() + room.getHeight() / 2)];
					RareTreasureChest chest = new RareTreasureChest(center);
					
					double chance = (treasure.size() * 10.0) / (enemies.size() + treasure.size() * 10.0);
					if (Math.random() < chance) {
						treasure.get((int)(Math.random() * treasure.size())).addItem(new KeyItem(0, 0, new Vector2(1, 1)));
					}
					else {
						enemies.get((int)(Math.random() * enemies.size())).addDeathLoot(new KeyItem(0, 0, new Vector2(1, 1)));
					}
					treasure.add(chest);
					addActor(chest);
				}
				else {
					//Spawn normal treasure
					FloorTile center = (FloorTile)tileMap[(int)(room.getX() + room.getWidth() / 2)][(int)(room.getY() + room.getHeight() / 2)];
					TreasureChest chest = new TreasureChest(center);
					treasure.add(chest);
					addActor(chest);
				}
			}
		}
		
		//Generate stairs
		boolean stairCreated = false;
		while (!stairCreated) {
			Rectangle randomRoom = rooms.get((int)(Math.random() * rooms.size()));
			int row = (int)(Math.random() * randomRoom.getWidth()) + (int)randomRoom.getX();
			int col = (int)(Math.random() * randomRoom.getHeight()) + (int)randomRoom.getY();
			
			if (tileMap[row][col].getCollisionBox().isEmpty()) {
				boolean locked = (Math.random() < 0.25 || treasure.isEmpty()) ? false : true;
				StairTile stairs = new StairTile(row, col, locked, environment);
				tileMap[row][col].remove();
				floorTiles.remove(tileMap[row][col]);
				roomTiles.remove(tileMap[row][col]);
				tileMap[row][col] = stairs;
				addActor(stairs);
				if (locked) {
					treasure.get((int)(Math.random() * treasure.size())).addItem(new KeyItem(0, 0, new Vector2(1, 1)));
				}
				stairCreated = true;
			}
		}
		
		//Spawn in players
		Rectangle randomRoom = rooms.get((int)(Math.random() * rooms.size()));
		for (PlayerCharacter player : PlayerCharacter.getPlayers()) {
			int row = (int)(Math.random() * randomRoom.getWidth()) + (int)randomRoom.getX();
			int col = (int)(Math.random() * randomRoom.getHeight()) + (int)randomRoom.getY();
			player.setPosition(tileMap[row][col].getCenter().x, tileMap[row][col].getCenter().y);
			player.getVelocity().setLength(0);
			player.removeHeldItem();
			addActor(player);
		}
		getViewport().setCamera(new FollowCam());
	}
	
	/**
	 * Create wall tiles if needed at the specified row and column
	 * @param row			The row to create the wall tile
	 * @param col			The column to create the wall tile
	 */
	private void createWallTile(int row, int col) {
		int[][] surrounding = new int[3][3];
		for (int r = -1; r <= 1; r++) {
			for (int c = -1; c <= 1; c++) {
				if (inBounds(row + r, col + c)) {
					surrounding[r + 1][c + 1] = map[row + r][col + c];
				}
				else {
					surrounding[r + 1][c + 1] = 1;
				}
			}
		}
		WallTile tile = new WallTile(row, col, surrounding, environment);
		tileMap[row][col] = tile;
		addActor(tile);
	}
	
	
	/**
	 * Returns a list of rectangles around a given coordinate that matches collision boxes
	 * @param x
	 * @param y
	 * @param range
	 * @return
	 */
	public ArrayList<Rectangle> getSurroundingCollisionBoxes(float x, float y, int range){
		ArrayList<Rectangle> boxes = new ArrayList<Rectangle>();
		float tileLength = AbstractTile.TILE_LENGTH;
		int row = (int)(y / tileLength);
		int col = (int)(x / tileLength);
		for (int r = row - range; r <= row + range; r++) {
			for (int c = col - range; c <= col + range; c++) {
				if (inBounds(r, c)) {
					boxes.addAll(tileMap[r][c].getCollisionBox());
				}
				else{
					Rectangle box = new Rectangle(c * tileLength, r * tileLength, tileLength, tileLength);
					boxes.add(box);
				}
			}
		}
		return boxes;
	}
	
	
	/**
	 * Returns a list of rectangles around a given coordinate that matches wall boxes only
	 * @param x
	 * @param y
	 * @param range
	 * @return
	 */
	public ArrayList<Rectangle> getSurroundingWallBoxes(float x, float y, int range){
		ArrayList<Rectangle> boxes = new ArrayList<Rectangle>();
		float tileLength = AbstractTile.TILE_LENGTH;
		int row = (int)(y / tileLength);
		int col = (int)(x / tileLength);
		for (int r = row - range; r <= row + range; r++) {
			for (int c = col - range; c <= col + range; c++) {
				if (inBounds(r, c)) {
					if (map[r][c] == 1) {
						boxes.addAll(tileMap[r][c].getCollisionBox());
					}
				}
				else{
					Rectangle box = new Rectangle(c * tileLength, r * tileLength, tileLength, tileLength);
					boxes.add(box);
				}
			}
		}
		return boxes;
	}
	
	/**
	 * Randomly places an actor on an open position
	 * @param a
	 */
	public void setRandomSpawn(Actor a) {
		Vector2 position = getRandomOpenPos();
		a.setPosition(position.x, position.y);
	}
	
	/**
	 * Get a random open position on the map
	 * @return
	 */
	public Vector2 getRandomOpenPos() {
		FloorTile randomTile = roomTiles.get((int)(Math.random() * roomTiles.size()));
		return randomTile.getCenter();
	}
	
	/**
	 * Checks if given tile is a room tile
	 */
	public boolean isRoomTile(int row, int col) {
		for (Rectangle room : rooms) {
			if (row >= room.getX() && row < room.getX() + room.getWidth() &&
					col >= room.getY() && col < room.getY() + room.getHeight()) return true;
		}
		return false;
	}
	
	/**
	 * Returns whether the given row and column tile is a wall
	 * @param row		The row to check
	 * @param col		The column to check
	 * @return 			Whether that is a wall or not
	 */
	private boolean isWall(int row, int col) {
		return (map[row][col] == 1);
	}
	
	/**
	 * Returns whether the the given space inside the map
	 * @param row		The row to check
	 * @param col		The column to check
	 * @return			Whether the space is in bounds of the map
	 */
	private boolean inBounds(int row, int col) {
		return (row >= 0 && row < map.length && col >= 0 && col < map[0].length);
	}
	
	public void setReadyForNextLevel(boolean flag) {
		readyForNextLevel = flag;
	}
	
	public boolean isReadyForNextLevel() {
		return readyForNextLevel;
	}
	
	@Override
	public void draw() {
		getActors().sort(comparator);
		super.draw();
	}
	
	@Override
	public void dispose() {
		while (getActors().size > 0) {
			getActors().pop().remove();
		}
		super.dispose();
	}
}

class ActorComparator implements Comparator<Actor>{

	@Override
	public int compare(Actor o1, Actor o2) {
		if (o1.getClass().equals(o2.getClass())) {
			if (o2.getY() > o1.getY()) return 1;
			else return -1;
		}
		if (o2 instanceof MagicCursor) return -1;
		if (o1 instanceof MagicCursor) return 1;
		if (o2 instanceof MagicRingMenu) return -1;
		if (o1 instanceof MagicRingMenu) return 1;
		if (o2 instanceof PlayerCharacter && ((PlayerCharacter)o2).isSpawningIn()) return -1;
		if (o1 instanceof PlayerCharacter && ((PlayerCharacter)o1).isSpawningIn()) return 1;
		if (o2 instanceof DeathAnimation) return -1;
		if (o1 instanceof DeathAnimation) return 1;
		if (o2 instanceof WallTile && !((WallTile)o2).isFrontWall()) return -1;
		if (o1 instanceof WallTile && !((WallTile)o1).isFrontWall()) return 1;
		if (o2 instanceof FloorTile) return 1;
		if (o1 instanceof FloorTile) return -1;
		if (o2 instanceof StairTile) return 1;
		if (o1 instanceof StairTile) return -1;
		if (o2 instanceof MapItem) return 1;
		if (o1 instanceof MapItem) return -1;
		if (o2 instanceof OnGroundProjectile) return 1;
		if (o1 instanceof OnGroundProjectile) return -1;
		else {
			if (o2.getY() > o1.getY()) return 1;
			else return -1;
		}
	}
	
}
