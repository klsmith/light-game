package io.github.klsmith.lightgame;

import java.util.ArrayList;
import java.util.List;

public class Level {

	private final int playerStartX;
	private final int playerStartY;

	private final List<Wall> walls;

	private final Grid grid;

	public Level(Grid grid, int[] level) {
		this.grid = grid;
		walls = new ArrayList<>();
		int px = 0;
		int py = 0;
		for (int y = 0; y < grid.rows; y++) {
			for (int x = 0; x < grid.columns; x++) {
				final int index = (y * grid.columns) + x;
				int marker = level[index];
				if (1 == marker) {
					px = x * grid.cellSize + (grid.cellSize / 2);
					py = y * grid.cellSize + (grid.cellSize / 2);
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
		playerStartX = px;
		playerStartY = py;
	}

	public Grid getGridSettings() {
		return grid;
	}

	public int getWidth() {
		return grid.columns * grid.cellSize;
	}

	public int getHeight() {
		return grid.rows * grid.cellSize;
	}

	public void resetPlayer(Player player) {
		player.x = playerStartX;
		player.y = playerStartY;
	}

	public List<Wall> getWalls() {
		return walls;
	}

}
