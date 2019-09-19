package io.github.klsmith.lightgame;

import java.util.ArrayList;
import java.util.List;

public class Level {

	private final Grid grid;
	private final Player player;
	private final List<Wall> walls;

	public Level(Grid grid, int[] level) {
		this.grid = grid;
		this.player = new Player();
		walls = new ArrayList<>();
		for (int y = 0; y < grid.rows; y++) {
			for (int x = 0; x < grid.columns; x++) {
				final int index = (y * grid.columns) + x;
				int marker = level[index];
				if (1 == marker) {
					player.x = x * grid.cellSize + (grid.cellSize / 2);
					player.y = y * grid.cellSize + (grid.cellSize / 2);
				}
				if (2 == marker || 3 == marker) {
					final boolean isCircle = 3 == marker ? true : false;
					final Wall wall = new Wall(grid.cellSize, isCircle);
					wall.x = x * grid.cellSize;
					wall.y = y * grid.cellSize;
					walls.add(wall);
				}
			}
		}
	}

	public Grid getGrid() {
		return grid;
	}

	public int getWidth() {
		return grid.columns * grid.cellSize;
	}

	public int getHeight() {
		return grid.rows * grid.cellSize;
	}

	public Player getPlayer() {
		return player;
	}

	public List<Wall> getWalls() {
		return walls;
	}

}
