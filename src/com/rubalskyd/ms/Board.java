package com.rubalskyd.ms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Board {
	public static class Point {
		int x, y;
		
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}
	
	private final static double MINE_PROBABILITY = .14; //0-1
	
	private final int height, width;
	private int[][] board; // -1 for mine
	// Used for BFS as well, don't need to reset as each spot
	// can only be visited max once throughout game
	private boolean[][] open;
	private boolean[][] marked;
	private boolean isGameOver = false;
	private boolean hasWon = false;
	private int mines = 0, moves = 0;
	List<Point> pointsToShowFromLastMove = new LinkedList<Point>();

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.board = new int[height][width];
		this.open = new boolean[height][width];
		this.marked = new boolean[height][width];
		
		//Place mines and set numbers
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (Math.random() < MINE_PROBABILITY && !wouldBlock(i, j)) {
					mines++;
					board[i][j] = -1;
					addOneToNeighbors(board, i, j);
				} 
			}
		}
	}
	
	private boolean wouldBlock(int y, int x) {
		for (Point p: getMineNeighbors(x, y)) {
			 if (isSurroundedBy7(p.x, p.y)) return true;
		}
		return false;
	}
	
	private void addOneToNeighbors(int[][] board, int y, int x) {
		for (Point p: getNonMineNeighbors(x, y)) {
			board[p.y][p.x]++;
		}
	}
	
	private List<Point> getNonMineNeighbors(int x, int y) {
		List<Point> neighbors = new ArrayList<Point>();
		for (int i = y - 1; i <= y + 1; i++) {
			for (int j = x - 1; j <= x + 1; j++) {
				if ((i != y || j != x) && checkDimensions(j, i) && !isMine(j, i)) neighbors.add(new Point(j, i));
			}
		}
		return neighbors;
	}
	
	private List<Point> getMineNeighbors(int x, int y) {
		List<Point> neighbors = new ArrayList<Point>();
		for (int i = y - 1; i <= y + 1; i++) {
			for (int j = x - 1; j <= x + 1; j++) {
				if ((i != y || j != x) && checkDimensions(j, i) && isMine(j, i)) neighbors.add(new Point(j, i));
			}
		}
		return neighbors;
	}
	
	private List<Point> getAllNeighborsAndEdges(int x, int y) {
		List<Point> neighbors = new ArrayList<Point>();
		for (int i = y - 1; i <= y + 1; i++) {
			for (int j = x - 1; j <= x + 1; j++) {
				if (i != y || j != x) neighbors.add(new Point(j, i));
			}
		}
		return neighbors;	
	}
	
	private boolean isSurroundedBy7(int x, int y) {
		int surroundedBy = 0;
		for (Point p: getAllNeighborsAndEdges(x, y)) {
			if (!checkDimensions(p.x, p.y) || isMine(p.x, p.y))
				surroundedBy++;
		}
		return surroundedBy == 7;
	}
	
	private boolean checkDimensions(int x, int y) {
		return (x >= 0 && x < width && y >= 0 && y < height);
	}
	
	private boolean isMine(int x, int y) {
		return board[y][x] == -1;
	}
	
	public boolean isGameOver() {
		return isGameOver;
	}
	
	public boolean hasWon() {
		return hasWon;
	}
		
	public String moveAndGet(int x, int y) {
		if (open[y][x]) return get(x, y);
		
		pointsToShowFromLastMove.clear();
		
		open[y][x] = true;
		
		if (isMine(x, y)) {
			isGameOver = true;
		} else if (board[y][x] == 0) {
			getSurroundings(x, y);
			for (Point p: pointsToShowFromLastMove) {
				open[p.y][p.x] = true;
			}
			moves += pointsToShowFromLastMove.size(); //Will include self
		} else {
			moves++;
		}
		
		if (moves == width * height - mines) {
			isGameOver = true;
			hasWon = true;
		}
		//System.out.println(moves + " spots uncovered! " + mines + " mines.");
		//System.out.println(pointsToShowFromLastMove);
		return get(x, y);
	}
	
	public void toggleMark(int x, int y) {
		marked[y][x] = !marked[y][x];
	}
	
	public boolean isMarked(int x, int y) {
		return marked[y][x] && !open[y][x];
	}
	
	public String get(int x, int y) {
		if (isMine(x, y)) return "*";
		return Integer.toString(board[y][x]);		
	}
	
	// Sets list of points to show
	private void getSurroundings(int x, int y) {
		if (board[y][x] != 0) return;
		
		Queue<Point> pointsToCheck = new LinkedList<Point>();
		
		pointsToCheck.add(new Point(x, y));
		open[y][x] = true;
		
		List<Point> neighbors;
		while (!pointsToCheck.isEmpty()) {
			Point p = pointsToCheck.poll();
			neighbors = getNonMineNeighbors(p.x, p.y); 
			for (Point n: neighbors) {
				if (!open[n.y][n.x]) {
					if (board[n.y][n.x] == 0) {
						pointsToCheck.add(n);
						//System.err.println("Adding to check " + n);
					} else {
						pointsToShowFromLastMove.add(n);
						//System.err.println(n + " closed");
					}
					open[n.y][n.x] = true;
				}
			}
			pointsToShowFromLastMove.add(p);
			//System.err.println(p + " closed");
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String showCur() {
		String s = "";
		s += "     ";
		for (int i = 0; i < width; i++) {
			s += String.format("%2d ", i);
		}
		s += "\n";
		s += "   --";
		for (int i = 0; i < width; i++) {
			s += "---";
		}
		s += "\n";
		for (int i = 0; i < height; i++) {
			s += String.format("%2d | ", i);
			for (int j = 0; j < width; j++) {
				if (open[i][j]) {
					if (board[i][j] == -1) s += " * ";
					else s += String.format("%2d ", board[i][j]);
				} else {
					if (marked[i][j]) {
						s += " * ";
					} else {
						s += " ? ";
					}
				}
			}
			s += "\n";
		}
		return s;
	}
	
	public String toString() {
		String s = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				s += (board[i][j] == -1) ? " * " : String.format("%2d", board[i][j]) + " ";
			}
			s += "\n";
		}
		return s;
	}
}
