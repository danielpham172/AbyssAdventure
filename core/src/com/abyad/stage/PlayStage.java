package com.abyad.stage;

import java.util.ArrayList;
import java.util.Comparator;

import com.abyad.actor.cosmetic.BattleText;
import com.abyad.actor.cosmetic.DeathAnimation;
import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.entity.ZombieCharacter;
import com.abyad.actor.mapobjects.RareTreasureChest;
import com.abyad.actor.mapobjects.ReviveStatue;
import com.abyad.actor.mapobjects.TreasureChest;
import com.abyad.actor.mapobjects.items.MapItem;
import com.abyad.actor.mapobjects.items.carrying.CorpseItem;
import com.abyad.actor.mapobjects.items.carrying.KeyItem;
import com.abyad.actor.projectile.OnGroundProjectile;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.actor.tile.StairTile;
import com.abyad.actor.tile.WallTile;
import com.abyad.actor.ui.MagicCursor;
import com.abyad.actor.ui.MagicRingMenu;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.mapdata.MapEnvironment;
import com.abyad.utils.DungeonGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * The PlayStage is derived from the Stage class. This creates the map for the game.
 *
 */
public class PlayStage extends Stage{

	protected AbyssAdventureGame game;				//The game object
	
	protected int[][] map;							//The int array map
	protected AbstractTile[][] tileMap;				//The map of the tiles
	protected ArrayList<Rectangle> rooms;				//The rooms (x = row, y = col)
	protected ArrayList<FloorTile> floorTiles;		//The floor tiles
	protected ArrayList<FloorTile> roomTiles;			//The room tiles
	
	protected ArrayList<TreasureChest> treasure;		//All treasure chests
	protected ArrayList<AbstractEntity> enemies;		//All enemies
	
	protected boolean readyForNextLevel;
	
	//Comparator object to organize the actors
	protected static ActorComparator comparator = new ActorComparator();
	protected static float ZOOM = 5f;
	
	public PlayStage(AbyssAdventureGame game, boolean generateFloor) {
		super(new ExtendViewport(1920, 1080));		//Creates the stage with a viewport
		this.game = game;
		if (generateFloor) generateFloor();
		getViewport().setCamera(new FollowCam(1 / ZOOM));
	}
	
	/**
	 * Constructor to create the stage
	 * @param game			The game object
	 */
	public PlayStage(AbyssAdventureGame game) {
		super(new ExtendViewport(1920, 1080));		//Creates the stage with a viewport
		this.game = game;
		generateFloor();
		getViewport().setCamera(new FollowCam(1 / ZOOM));
	}
	
	public void generateFloor() {
		MapEnvironment environment = MapEnvironment.environments.get(MapEnvironment.environments.keySet().toArray(new String[] {})[(int)(Math.random() * MapEnvironment.environments.size())]);
		
		DungeonGenerator generator = new DungeonGenerator(18, 18, 3, 5, 600);		//Create a generator
		generator.runDungeonGenerator();		//Generate the dungeon
		map = generator.getDungeon();			//Set the map
		tileMap = new AbstractTile[map.length][map[0].length];
		floorTiles = new ArrayList<FloorTile>();
		roomTiles = new ArrayList<FloorTile>();
		ArrayList<FloorTile> spawnRoomTiles = new ArrayList<FloorTile>();
		rooms = generator.getRooms();
		
		Rectangle playerSpawnRoom = rooms.remove((int)(Math.random() * rooms.size()));
		if (rooms.size() == 0) rooms.add(playerSpawnRoom);
		
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				if (map[r][c] == 0) {
					FloorTile tile = environment.getFloorTile(map, r, c);
					addActor(tile);
					tileMap[r][c] = tile;
					floorTiles.add(tile);
					if (isRoomTile(r, c)) {
						roomTiles.add(tile);
					}
					if (r >= playerSpawnRoom.getX() && r < playerSpawnRoom.getX() + playerSpawnRoom.getWidth() &&
							c >= playerSpawnRoom.getY() && c < playerSpawnRoom.getY() + playerSpawnRoom.getHeight()) {
						spawnRoomTiles.add(tile);
					}
				}
				if (map[r][c] == 1) {
					WallTile tile = environment.getWallTile(map, r, c);
					addActor(tile);
					tileMap[r][c] = tile;
				}
			}
		}
		
		//Spawn in players
		for (PlayerCharacter player : PlayerCharacter.getPlayers()) {
			int row = (int)(Math.random() * playerSpawnRoom.getWidth()) + (int)playerSpawnRoom.getX();
			int col = (int)(Math.random() * playerSpawnRoom.getHeight()) + (int)playerSpawnRoom.getY();
			player.setPosition(tileMap[row][col].getCenter().x, tileMap[row][col].getCenter().y);
			player.getVelocity().setLength(0);
			player.removeHeldItem();
			
			if (!player.isDead()) {
				addActor(player);
			}
			else {
				Vector2 randomDirection = (new Vector2(0.01f, 0)).setToRandomDirection();
				CorpseItem corpse = player.getCorpse(randomDirection);
				addActor(corpse);
				corpse.spawn();
			}
			
			if (rooms.contains(playerSpawnRoom)) {
				roomTiles.remove(tileMap[row][col]);
			}
		}
		
		for (int i = 0; i < PlayerCharacter.getPlayers().size(); i++) {
			if (PlayerCharacter.getPlayers().get(i).isDead()) {
				PlayerCharacter.getPlayers().remove(i);
				i--;
			}
		}
		
		//Generate enemies
		enemies = new ArrayList<AbstractEntity>();
		for (int i = 0; i < 100; i++) {
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
			FloorTile center = (FloorTile)tileMap[(int)(room.getX() + room.getWidth() / 2)][(int)(room.getY() + room.getHeight() / 2)];
			if (Math.random() < 0.9) {
				if (Math.random() < 0.1 && (!enemies.isEmpty() || !treasure.isEmpty())) {
					//Spawn rare treasure
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
					TreasureChest chest = new TreasureChest(center);
					treasure.add(chest);
					addActor(chest);
				}
			}
			else {
				ReviveStatue statue = new ReviveStatue(center);
				addActor(statue);
			}
		}
		
		//Generate stairs
		boolean stairCreated = false;
		while (!stairCreated) {
			Rectangle randomRoom = rooms.get((int)(Math.random() * rooms.size()));
			int row = (int)(Math.random() * randomRoom.getWidth()) + (int)randomRoom.getX();
			int col = (int)(Math.random() * randomRoom.getHeight()) + (int)randomRoom.getY();
			
			if (tileMap[row][col].getCollisionBox().isEmpty()) {
				boolean locked = (Math.random() < 0.3 || treasure.isEmpty()) ? false : true;
				//StairTile stairs = new StairTile(row, col, locked, environment);
				StairTile stairs = environment.getStairs(row, col, locked);
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
		
		if (!rooms.contains(playerSpawnRoom)) {
			roomTiles.addAll(spawnRoomTiles);
			rooms.add(playerSpawnRoom);
		}
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
	
	public AbstractTile getTileAt(int row, int col) {
		if (inBounds(row, col)) {
			return tileMap[row][col];
		}
		else {
			return null;
		}
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
		//long initial = System.nanoTime();
		//comparator.quicksortActorArray(getActors());
		try {
			getActors().sort(comparator);
		}
		catch (IllegalArgumentException e){
			//If normal sorting fails, just use own quicksort instead (this one is pretty slow compared to the
			//in Java one, like 5 to 20 times slower or something
			comparator.quicksortActorArray(getActors());
		}
		//System.out.println(System.nanoTime() - initial);
		super.draw();
	}
	
	@Override
	public void dispose() {
		int size = getActors().size;
		for (int i = 0; i < size; i++) {
			getActors().get(0).remove();
		}
		super.dispose();
	}
}

class ActorComparator implements Comparator<Actor>{

	public void quicksortActorArray(Array<Actor> actors) {
		quicksortActorArray(actors, 0, actors.size - 1);
	}
	
	public void quicksortActorArray(Array<Actor> actors, int lo, int hi) {
		if (lo < hi) {
			int p = partitionArray(actors, lo, hi);
			quicksortActorArray(actors, lo, p);
			quicksortActorArray(actors, p + 1, hi);
		}
	}
	
	public int partitionArray(Array<Actor> actors, int lo, int hi) {
		Actor pivot = actors.get((lo + hi) / 2);
		int lower = lo - 1;
		int higher = hi + 1;
		
		while (true) {
			do {
				lower++;
			} while (compare(actors.get(lower), pivot) < 0);
			
			do {
				higher--;
			} while (compare(actors.get(higher), pivot) > 0);
			
			if (lower >= higher) {
				return higher;
			}
			else {
				actors.swap(lower, higher);
			}
		}
	}
	
	@Override
	public int compare(Actor o1, Actor o2) {
		if (o1.getClass().getName().equals(o2.getClass().getName()) &&
				!(o1 instanceof PlayerCharacter && ((PlayerCharacter)o1).isSpawningIn()) &&
				!(o2 instanceof PlayerCharacter && ((PlayerCharacter)o2).isSpawningIn()) &&
				!(o1 instanceof WallTile && !((WallTile)o1).isFrontWall()) &&
				!(o2 instanceof WallTile && !((WallTile)o2).isFrontWall())) {
			return (int)((o2.getY() - o1.getY()));
		}
		
		if (o1 instanceof BattleText) return 1;
		if (o2 instanceof BattleText) return -1;
		if (o1 instanceof MagicCursor) return 1;
		if (o2 instanceof MagicCursor) return -1;
		if (o1 instanceof MagicRingMenu) return 1;
		if (o2 instanceof MagicRingMenu) return -1;
		if (o1 instanceof PlayerCharacter && ((PlayerCharacter)o1).isSpawningIn()) {
			if (o2 instanceof PlayerCharacter && ((PlayerCharacter)o2).isSpawningIn())
				return (int)((o2.getY() - o1.getY())); 
			else return 1;
		}
		if (o2 instanceof PlayerCharacter && ((PlayerCharacter)o2).isSpawningIn()) return -1;
		if (o1 instanceof DeathAnimation) return 1;
		if (o2 instanceof DeathAnimation) return -1;
		if (o1 instanceof WallTile && !((WallTile)o1).isFrontWall()) {
			if (o2 instanceof WallTile && !((WallTile)o2).isFrontWall()) {
				return (int)((o2.getY() - o1.getY())); 
			}
			return 1;
		}
		if (o2 instanceof WallTile && !((WallTile)o2).isFrontWall()) return -1;
		
		if (o1 instanceof FloorTile) return -1;
		if (o2 instanceof FloorTile) return 1;
		if (o1 instanceof StairTile) return -1;
		if (o2 instanceof StairTile) return 1;
		if (o1 instanceof MapItem) return -1;
		if (o2 instanceof MapItem) return 1;
		if (o1 instanceof OnGroundProjectile) return -1;
		if (o2 instanceof OnGroundProjectile) return 1;
		
		else {
			return (int)((o2.getY() - o1.getY()));
		}
	}
	
}
