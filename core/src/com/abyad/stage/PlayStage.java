package com.abyad.stage;

import java.util.ArrayList;
import java.util.Comparator;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.actor.tile.WallTile;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.utils.DungeonGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PlayStage extends Stage{

	private AbyssAdventureGame game;
	private int[][] map;
	private AbstractTile[][] tileMap;
	private ArrayList<FloorTile> floorTiles;
	
	private static Comparator<Actor> comparator = new Comparator<Actor>() {
		@Override
		public int compare(Actor o1, Actor o2) {
			if (o2 instanceof FloorTile) return 1;
			if (o1 instanceof FloorTile) return -1;
			if (o2 instanceof WallTile && !((WallTile)o2).isFrontWall()) return -1;
			if (o1 instanceof WallTile && !((WallTile)o1).isFrontWall()) return 1;
			else return (int)(o2.getY() - o1.getY());
		}
	};
	
	public PlayStage(AbyssAdventureGame game) {
		super(new ExtendViewport(384, 216));
		this.game = game;
		
		DungeonGenerator generator = new DungeonGenerator(15, 15, 2, 6, 600);
		generator.runDungeonGenerator();
		map = generator.getDungeon();
		map = createBorder(expandMap(map), 1);
		tileMap = new AbstractTile[map.length][map[0].length];
		floorTiles = new ArrayList<FloorTile>();
		
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				if (map[r][c] == 0) {
					FloorTile tile = new FloorTile(r, c);
					addActor(tile);
					tileMap[r][c] = tile;
					floorTiles.add(tile);
				}
				if (map[r][c] == 1) {
					createWallTile(r, c);
				}
			}
		}
		
		getViewport().setCamera(new FollowCam());
	}
	
	private void createWallTile(int row, int col) {
		int[][] surrounding = new int[3][3];
		boolean needTile = false;
		for (int r = -1; r <= 1; r++) {
			for (int c = -1; c <= 1; c++) {
				if (inBounds(row + r, col + c)) {
					if (map[row + r][col + c] == 0) needTile = true;
					surrounding[r + 1][c + 1] = map[row + r][col + c];
				}
				else {
					surrounding[r + 1][c + 1] = 1;
				}
			}
		}
		
		if (needTile) {
			WallTile tile = new WallTile(row, col, surrounding);
			addActor(tile);
		}
	}
	
	private int[][] expandMap (int[][] map){
		int[][] bigMap = new int[map.length * 3 - 2][map[0].length * 3 - 2];
		for (int r = 0; r < bigMap.length; r++) {
			for (int c = 0; c < bigMap.length; c++) {
				bigMap[r][c] = 1;
			}
		}
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				bigMap[row * 3][col * 3] = map[row][col];
				if (map[row][col] == 0) {
					if (col != 0) {
						if (map[row][col - 1] == 0) {
							bigMap[row * 3][col * 3 - 1] = 0;
							bigMap[row * 3][col * 3 - 2] = 0;
						}
					}
					if (row != 0) {
						if (map[row - 1][col] == 0) {
							bigMap[row * 3 - 1][col * 3] = 0;
							bigMap[row * 3 - 2][col * 3] = 0;
						}
					}
					if (row != 0 && col != 0) {
						if (map[row][col - 1] == 0 && map[row - 1][col] == 0 && map[row - 1][col - 1] == 0) {
							bigMap[row * 3 - 1][col * 3 - 1] = 0;
							bigMap[row * 3 - 1][col * 3 - 2] = 0;
							bigMap[row * 3 - 2][col * 3 - 1] = 0;
							bigMap[row * 3 - 2][col * 3 - 2] = 0;
						}
					}
				}
			}
		}
		return bigMap;
	}
	
	private int[][] createBorder(int[][] map, int borderSize){
		int[][] borderedMap = new int[map.length + (borderSize * 2)][map[0].length + (borderSize * 2)];
		for (int r = 0; r < borderedMap.length; r++) {
			for (int c = 0; c < borderedMap[r].length; c++) {
				if (r < borderSize || r >= borderedMap.length - borderSize || c < borderSize || c >= borderedMap[r].length - borderSize) {
					borderedMap[r][c] = 1;
				}
				else {
					borderedMap[r][c] = map[r - borderSize][c - borderSize];
				}
			}
		}
		return borderedMap;
	}
	
	public ArrayList<Rectangle> getSurroundingWallBoxes(float x, float y, int range){
		ArrayList<Rectangle> boxes = new ArrayList<Rectangle>();
		float tileLength = AbstractTile.TILE_LENGTH;
		int row = (int)(y / tileLength);
		int col = (int)(x / tileLength);
		for (int r = row - range; r <= row + range; r++) {
			for (int c = col - range; c <= col + range; c++) {
				if (inBounds(r, c)) {
					if (isWall(r, c)) {
						Rectangle box = new Rectangle(c * tileLength, r * tileLength, tileLength, tileLength);
						boxes.add(box);
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
	
	public void setRandomSpawn(Actor a) {
		Vector2 position = getRandomOpenPos();
		a.setPosition(position.x, position.y);
	}
	
	public Vector2 getRandomOpenPos() {
		FloorTile randomTile = floorTiles.get((int)(Math.random() * floorTiles.size()));
		return randomTile.getCenter();
	}
	
	private boolean isWall(int row, int col) {
		return (map[row][col] == 1);
	}
	
	private boolean inBounds(int row, int col) {
		return (row >= 0 && row < map.length && col >= 0 && col < map[0].length);
	}
	
	@Override
	public void addActor(Actor actor) {
		if (actor instanceof AbstractEntity) {
			setRandomSpawn(actor);
		}
		super.addActor(actor);
	}
	
	@Override
	public void draw() {
		getActors().sort(comparator);
		super.draw();
	}
	
	@Override
	public void dispose() {
		for (Actor actor : getActors()) {
			actor.remove();
		}
		super.dispose();
	}
}
