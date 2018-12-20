package com.abyad.utils;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;


public class DungeonGenerator {

	private int[][] dungeon;
	private int[][] colorMapDungeon;
	private int color;
	private int rows;
	private int cols;
	private int minRoomSize;
	private int maxRoomSize;
	private int density;
	
	private ArrayList<Node> connections;
	private ArrayList<Rectangle> rooms;
	
	public DungeonGenerator(int rows, int cols, int minRoomSize, int maxRoomSize, int density) {
		this.rows = rows;
		this.cols = cols;
		this.minRoomSize = minRoomSize;
		this.maxRoomSize = maxRoomSize;
		this.density = density;
		color = 1;
		dungeon = new int[rows][cols];
		colorMapDungeon = new int[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				dungeon[r][c] = 1;
			}
		}
	}
	
	public int[][] getDungeon(){
		return dungeon;
	}
	
	public ArrayList<Rectangle> getRooms(){
		return rooms;
	}
	
	public void runDungeonGenerator() {
		buildRooms();
		buildCorridors();
		colorizeDungeon();
		findConnectionPoints();
		connectDungeon();
		//Standard Clear
		pruneDeadEnds();
		fillHoles();
		verifyRooms();
		System.out.println(rooms.size());
		expandMap();
		createBorder(1);
		verifyRooms();
		System.out.println(rooms.size());
		//Cellular Automata
		//cellularAutomataRun();
		colorMapDungeon = new int[rows][cols];
	}
	
	public void cellularAutomataRun() {
		cellularAutomata(Math.min(rows * cols, 1000), 3, 6);
		colorMapDungeon = new int[rows][cols];
		buildCorridors();
		cellularAutomata(Math.min(rows * cols, 1000), 2, 6);
		buildCorridors();
		colorizeDungeon();
		findConnectionPoints();
		connectDungeon();
		pruneDeadEnds();
		connectHoles();
		colorMapDungeon = new int[rows][cols];
	}
	
	private void buildRooms() {
		rooms = new ArrayList<Rectangle>();
		for (int count = 0; count < density; count++) {
			int row = (int)(Math.random() * rows);
			int col = (int)(Math.random() * cols);
			int length = (int)(Math.pow(Math.random(), /*cols/rows*/ 1) * (maxRoomSize - minRoomSize + 1)) + minRoomSize;
			int height = (int)(Math.pow(Math.random(), /*rows/cols*/ 1) * (maxRoomSize - minRoomSize + 1)) + minRoomSize;
			if (canBuild(row, col, length, height)) {
				Rectangle room = new Rectangle(row, col, length, height);
				rooms.add(room);
				openRoom(row, col, length, height);
			}
		}
	}
	
	private boolean canBuild(int row, int col, int length, int height) {
		if (row + length > rows || col + height > cols) return false;
		if (row < 0 || col < 0) return false;
		for (int r = Math.max(row - 1, 0); r < Math.min(row + length + 1, rows); r++) {
			for (int c = Math.max(col - 1, 0); c < Math.min(col + height + 1, cols); c++){
				if (dungeon[r][c] == 0) return false;
			}
		}
		return true;
	}
	
	private boolean isFilled(int row1, int col1, int row2, int col2, int tile) {
		int tRow = Math.min(row1, row2);
		int bRow = Math.max(row1, row2);
		int lCol = Math.min(col1, col2);
		int rCol = Math.max(col1, col2);
		for (int r = tRow; r <= bRow; r++) {
			for (int c = lCol; c <= rCol; c++) {
				if (inBounds(r, c) && getTile(r, c) != tile) return false;
			}
		}
		return true;
	}
	
	private void openRoom(int row, int col, int length, int height) {
		for (int r = row; r < row + length; r++) {
			for (int c = col; c < col + height; c++) {
				dungeon[r][c] = 0;
			}
		}
	}
	
	private void buildCorridors() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (canBuild(r, c, 1, 1)) {
					runMazeBuilder(r, c);
				}
			}
		}
	}
	
	private void runMazeBuilder(int sRow, int sCol) {
		ArrayList<Node> path = new ArrayList<Node>();
		path.add(new Node(sRow, sCol));
		while (!path.isEmpty()) {
			Node current = path.remove(path.size() - 1);
			if (current.hasNextDirection()) {
				int[] nextDirection = current.getNextDirection();
				int nRow = current.row + nextDirection[0];
				int nCol = current.col + nextDirection[1];
				if (inBounds(nRow, nCol) && getTile(nRow, nCol) == 1) {
					if (nRow == current.row && isFilled(nRow - 1, nCol, nRow + 1, nCol + nextDirection[1], 1)) {
						dungeon[nRow][nCol] = 0;
						Node newNode = new Node(nRow, nCol);
						path.add(current);
						current = newNode;
					}
					else if (nCol == current.col && isFilled(nRow, nCol - 1, nRow + nextDirection[0], nCol + 1, 1)) {
						dungeon[nRow][nCol] = 0;
						Node newNode = new Node(nRow, nCol);
						path.add(current);
						current = newNode;
					}
				}
				path.add(current);
			}
		}
	}
	
	private void colorizeDungeon() {
		color = 1;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (getTile(r, c) == 0 && colorMapDungeon[r][c] == 0) {
					colorFlood(r, c, color);
					color++;
				}
			}
		}
	}
	
	private void colorFlood(int sRow, int sCol, int color) {
		ArrayList<Node> queue = new ArrayList<Node>();
		queue.add(new Node(sRow, sCol));
		while (!queue.isEmpty()) {
			Node node = queue.remove(0);
			colorMapDungeon[node.row][node.col] = color;
			while (node.hasNextDirection()) {
				int[] dir = node.getNextDirection();
				int nRow = node.row + dir[0];
				int nCol = node.col + dir[1];
				if (inBounds(nRow, nCol) && getTile(nRow, nCol) == 0 && colorMapDungeon[nRow][nCol] == 0) {
					colorMapDungeon[nRow][nCol] = color;
					Node newNode = new Node(nRow, nCol);
					queue.add(newNode);
				}
			}
		}
	}
	
	private void findConnectionPoints() {
		connections = new ArrayList<Node>();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (getTile(r, c) == 1 && countSurrounding(r, c, 0) == 2) {
					int[] colors = getColorSurrounding(r, c);
					if (colors.length == 2) connections.add(new Node(r, c, colors[0], colors[1]));
				}
			}
		}
	}
	
	private void connectDungeon() {
		boolean[] connected = new boolean[color];
		boolean[][] fullConnected = new boolean[color][color];
		
		connected[1] = true;
		ArrayList<Node> possible = new ArrayList<Node>();
		addValidConnections(possible, 1);
		while (!possible.isEmpty()) {
			Node randomConnector = possible.remove((int)(Math.random() * possible.size()));
			int color1 = randomConnector.color1;
			int color2 = randomConnector.color2;
			int row = randomConnector.row;
			int col = randomConnector.col;
			if (!fullConnected[color1][color2]) {
				if (connected[color1] && connected[color2] && Math.random() < 0.07) {
					fullConnected[color1][color2] = true;
					dungeon[row][col] = 0;
				}
				else {
					if (!connected[color1]) addValidConnections(possible, color1);
					if (!connected[color2]) addValidConnections(possible, color2);
					connected[color1] = true;
					connected[color2] = true;
					fullConnected[color1][color2] = true;
					dungeon[row][col] = 0;
				}
			}
			else {
				if (countSurrounding(row, col, 0) == 2 && Math.random() < 0.02) {
					dungeon[row][col] = 0;
				}
			}
		}
	}
	
	private void addValidConnections(ArrayList<Node> possible, int color) {
		int i = 0;
		while (i < connections.size()) {
			Node node = connections.get(i);
			if (node.connectsTo(color)) {
				possible.add(node);
				connections.remove(i);
			}
			else {
				i++;
			}
		}
	}
	
	private void pruneDeadEnds() {
		boolean change = true;
		while (change) {
			change = false;
			int[][] newDungeon = new int[rows][cols];
			int[][] newColorMap = new int[rows][cols];
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					if (getTile(r, c) == 0 && countSurrounding(r, c, 0) <= 1) {
						newDungeon[r][c] = 1;
						newColorMap[r][c] = 0;
						change = true;
					}
					else {
						newDungeon[r][c] = getTile(r, c);
						newColorMap[r][c] = getTileColor(r, c);
					}
				}
			}
			dungeon = newDungeon;
			colorMapDungeon = newColorMap;
		}
	}
	
	private void cellularAutomata(int times, int death, int birth) {
		for (int count = 0; count < times; count++) {
			boolean change = false;
			int[][] newDungeon = new int[rows][cols];
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					int surround = countSurrounding(r, c, 0, false);
					int dSurround = countSurrounding(r, c, 0, true);
					if (dSurround <= death) {
						newDungeon[r][c] = 1;
						change = true;
					}
					else if (dSurround >= birth) {
						newDungeon[r][c] = 0;
						change = true;
					}
					else {
						newDungeon[r][c] = getTile(r, c);
					}
				}
			}
			dungeon = newDungeon;
			if (!change) return;
		}
	}
	
	private void fillHoles() {
		ArrayList<ArrayList<Node>> holes = getHoles();
		
		if (holes.size() > 1) {
			int largestSize = holes.get(0).size();
			int largestIndex = 0;
			for (int i = 1; i < holes.size(); i++) {
				if (holes.get(i).size() > largestSize) {
					largestSize = holes.get(i).size();
					largestIndex = i;
				}
			}
			holes.remove(largestIndex);
			
			for (ArrayList<Node> hole : holes) {
				for (Node node : hole) {
					dungeon[node.row][node.col] = 1;
				}
			}
		}
	}
	
	private void verifyRooms() {
		int index = 0;
		while (index < rooms.size()) {
			Rectangle room = rooms.get(index);
			boolean exists = true;
			for (int r = (int)(room.getX()); r < room.getX() + room.getWidth(); r++) {
				for (int c = (int)(room.getY()); c < room.getY() + room.getHeight(); c++) {
					if (dungeon[r][c] == 1) {
						exists = false;
					}
				}
			}
			if (!exists) rooms.remove(index); else index++;
		}
	}
	
	/**
	 * Method to expand an int array map
	 * @return	An expanded map
	 */
	private void expandMap(){
		int[][] bigMap = new int[dungeon.length * 3 - 2][dungeon[0].length * 3 - 2];
		for (int r = 0; r < bigMap.length; r++) {
			for (int c = 0; c < bigMap.length; c++) {
				bigMap[r][c] = 1;
			}
		}
		rows = rows * 3 - 2;
		cols = cols * 3 - 2;
		for (int row = 0; row < dungeon.length; row++) {
			for (int col = 0; col < dungeon[row].length; col++) {
				bigMap[row * 3][col * 3] = dungeon[row][col];
				if (dungeon[row][col] == 0) {
					if (col != 0) {
						if (dungeon[row][col - 1] == 0) {
							bigMap[row * 3][col * 3 - 1] = 0;
							bigMap[row * 3][col * 3 - 2] = 0;
						}
					}
					if (row != 0) {
						if (dungeon[row - 1][col] == 0) {
							bigMap[row * 3 - 1][col * 3] = 0;
							bigMap[row * 3 - 2][col * 3] = 0;
						}
					}
					if (row != 0 && col != 0) {
						if (dungeon[row][col - 1] == 0 && dungeon[row - 1][col] == 0 && dungeon[row - 1][col - 1] == 0) {
							bigMap[row * 3 - 1][col * 3 - 1] = 0;
							bigMap[row * 3 - 1][col * 3 - 2] = 0;
							bigMap[row * 3 - 2][col * 3 - 1] = 0;
							bigMap[row * 3 - 2][col * 3 - 2] = 0;
						}
					}
				}
			}
		}
		dungeon = bigMap;
		
		for (Rectangle room : rooms) {
			room.setX((int)(room.getX() * 3));
			room.setY((int)(room.getY() * 3));
			room.setWidth((int)(room.getWidth() * 3) - 2);
			room.setHeight((int)(room.getHeight() * 3) - 2);
		}
	}
	
	/**
	 * Draws a wall border around a map
	 * @param borderSize
	 * @return
	 */
	private void createBorder(int borderSize){
		int[][] borderedMap = new int[dungeon.length + (borderSize * 2)][dungeon[0].length + (borderSize * 2)];
		for (int r = 0; r < borderedMap.length; r++) {
			for (int c = 0; c < borderedMap[r].length; c++) {
				if (r < borderSize || r >= borderedMap.length - borderSize || c < borderSize || c >= borderedMap[r].length - borderSize) {
					borderedMap[r][c] = 1;
				}
				else {
					borderedMap[r][c] = dungeon[r - borderSize][c - borderSize];
				}
			}
		}
		dungeon = borderedMap;
		
		for (Rectangle room : rooms) {
			room.setX(room.getX() + borderSize);
			room.setY(room.getY() + borderSize);
		}
	}
	
	private void connectHoles() {
		ArrayList<ArrayList<Node>> holes = getHoles();
		if (holes.size() > 1) {
			System.out.println("Connecting holes!");
			ArrayList<Node> hole1 = holes.remove((int)(Math.random() * holes.size()));
			ArrayList<Node> hole2 = holes.remove((int)(Math.random() * holes.size()));
			
			Node rs1 = hole1.remove((int)(Math.random() * hole1.size()));
			Node rs2 = hole2.remove((int)(Math.random() * hole2.size()));
			int rowDir = (rs2.row - rs1.row);
			if (rowDir != 0) rowDir /= Math.abs(rs2.row - rs1.row);
			int colDir = (rs2.col - rs1.col);
			if (colDir != 0) colDir /= Math.abs(rs2.col - rs1.col);
			
			int row = rs1.row;
			int col = rs1.col;
			
			while (row != rs2.row || col != rs2.col) {
				int rDist = Math.abs(row - rs2.row);
				int cDist = Math.abs(col - rs2.col);
				if (row == rs2.row) {
					col += colDir;
				}
				else if (col == rs2.col) {
					row += rowDir;
				}
				else {
					if (Math.random() < (double)(rDist) / (rDist + cDist)) row += rowDir; else col += colDir;
				}
				dungeon[row][col] = 0;
			}
		}
	}
	
	private ArrayList<ArrayList<Node>> getHoles(){
		ArrayList<ArrayList<Node>> holes = new ArrayList<ArrayList<Node>>();
		boolean[][] checked = new boolean[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (getTile(r, c) == 0 && !checked[r][c]) {
					holes.add(getFloodedNodes(r, c, checked));
				}
			}
		}
		return holes;
	}
	private ArrayList<Node> getFloodedNodes(int sRow, int sCol, boolean[][] checked){
		ArrayList<Node> queue = new ArrayList<Node>();
		ArrayList<Node> all = new ArrayList<Node>();
		queue.add(new Node(sRow, sCol));
		while (!queue.isEmpty()) {
			Node node = queue.remove(0);
			checked[node.row][node.col] = true;
			while (node.hasNextDirection()) {
				int[] dir = node.getNextDirection();
				int nRow = node.row + dir[0];
				int nCol = node.col + dir[1];
				if (inBounds(nRow, nCol) && getTile(nRow, nCol) == 0 && checked[nRow][nCol] == false) {
					checked[nRow][nCol] = true;
					Node newNode = new Node(nRow, nCol);
					queue.add(newNode);
				}
			}
			all.add(node);
		}
		return all;
	}
	
	public int getWidth() {
		return cols;
	}
	
	public int getHeight() {
		return rows;
	}
	
	public int getTile(int row, int col) {
		return dungeon[row][col];
	}
	
	public int getTileColor(int row, int col) {
		return colorMapDungeon[row][col];
	}
	
	private int countSurrounding(int row, int col, int tile) {
		return countSurrounding(row, col, tile, false);
	}
	
	private int countSurrounding(int row, int col, int tile, boolean diagonal) {
		int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
		int count = 0;
		for (int[] dir : dirs) {
			int nRow = row + dir[0];
			int nCol = col + dir[1];
			if (inBounds(nRow, nCol) && getTile(nRow, nCol) == tile) count++;
		}
		if (diagonal) {
			int[][] dDirs = { {1, 1}, {-1, 1}, {1, -1}, {-1, -1} };
			for (int[] dir : dDirs) {
				int nRow = row + dir[0];
				int nCol = col + dir[1];
				if (inBounds(nRow, nCol) && getTile(nRow, nCol) == tile) count++;
			}
		}
		return count;
	}
	
	private int[] getColorSurrounding(int row, int col) {
		int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
		ArrayList<Integer> colors = new ArrayList<Integer>();
		for (int[] dir : dirs) {
			int nRow = row + dir[0];
			int nCol = col + dir[1];
			if (inBounds(nRow, nCol) && getTile(nRow, nCol) == 0) {
				int tileColor = getTileColor(nRow, nCol);
				boolean hasSeen = false;
				for (int color : colors) {
					if (color == tileColor) hasSeen = true;
				}
				if (!hasSeen && tileColor != 0) {
					colors.add(tileColor);
				}
			}
		}
		if (colors.size() == 0) return null;
		int[] colorsArray = new int[colors.size()];
		for (int i = 0; i < colors.size(); i++) {
			colorsArray[i] = colors.get(i);
		}
		return colorsArray;
	}
	
	public boolean inBounds(int row, int col) {
		return (row >= 0 && row < rows && col >= 0 && col < cols);
	}
}

class Node {
	int row;
	int col;
	
	int color1;
	int color2;
	
	private ArrayList<int[]> randomDirections;
	
	public Node(int row, int col) {
		this.row = row;
		this.col = col;
		randomDirections = new ArrayList<int[]>();
		randomDirections.add(new int[] {1, 0});
		randomDirections.add(new int[] {-1, 0});
		randomDirections.add(new int[] {0, 1});
		randomDirections.add(new int[] {0, -1});
	}
	
	public Node(int row, int col, int c1, int c2) {
		this.row = row;
		this.col = col;
		color1 = Math.min(c1, c2);
		color2 = Math.max(c1, c2);
		randomDirections = new ArrayList<int[]>();
		randomDirections.add(new int[] {1, 0});
		randomDirections.add(new int[] {-1, 0});
		randomDirections.add(new int[] {0, 1});
		randomDirections.add(new int[] {0, -1});
	}
	
	public boolean hasNextDirection() {
		return !randomDirections.isEmpty();
	}
	
	public int[] getNextDirection() {
		return randomDirections.remove((int)(Math.random() * randomDirections.size()));
	}
	
	public boolean connectsTo(int color) {
		return (color1 == color || color2 == color);
	}
	
	public boolean connectsTo(int c1, int c2) {
		return ((color1 == c1 && color2 == c2) || (color1 == c2 && color2 == c1));
	}
}
