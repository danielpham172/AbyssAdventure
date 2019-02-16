package com.abyad.mapdata;

import java.util.ArrayList;

public class TileDataTree {

	private TileDataTree left;			//0
	private TileDataTree right;			//1
	//Any other number: both
	
	private ArrayList<TileData> tiles;
	private int depth;
	
	private static final int MAX_DEPTH = 8;
	
	public TileDataTree(int depth) {
		this.depth = depth;
		if (isEnd()) tiles = new ArrayList<TileData>();
	}
	
	public void addTileData(TileData data, int surroundings) {
		if (!isEnd()) {
			int branchDecision = surroundings % 10;
			if (branchDecision == 0) {
				if (left == null) left = new TileDataTree(depth + 1);
				left.addTileData(data, surroundings / 10);
			}
			else if (branchDecision == 1) {
				if (right == null) right = new TileDataTree(depth + 1);
				right.addTileData(data, surroundings / 10);
			}
			else {
				if (left == null) left = new TileDataTree(depth + 1);
				if (right == null) right = new TileDataTree(depth + 1);
				left.addTileData(data, surroundings / 10);
				right.addTileData(data, surroundings / 10);
			}
		}
		else {
			tiles.add(data);
		}
	}
	
	public ArrayList<TileData> getTileDatas(int surroundings){
		if (!isEnd()) {
			ArrayList<TileData> chosenTiles = new ArrayList<TileData>();
			int branchDecision = surroundings % 10;
			if (branchDecision == 0 && left != null) {
				chosenTiles.addAll(left.getTileDatas(surroundings / 10));
			}
			else if (branchDecision == 1 && right != null) {
				chosenTiles.addAll(right.getTileDatas(surroundings / 10));
			}
			return chosenTiles;
		}
		else {
			return tiles;
		}
	}
	
	private boolean isEnd() {
		return depth == MAX_DEPTH;
	}
}
