package io.github.klsmith.lightgame;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Level {

	private final int startX;
	private final int startY;
	private final LightGame game;
	private final Grid grid;
	private final List<Wall> walls;

	public Level(LightGame game, Grid grid, int[] level) {
		this.game = game;
		this.grid = grid;
		walls = new ArrayList<>();
		int sx = 0;
		int sy = 0;
		for (int y = 0; y < grid.rows; y++) {
			for (int x = 0; x < grid.columns; x++) {
				final int index = (y * grid.columns) + x;
				int marker = level[index];
				if (1 == marker) {
					sx = x * grid.cellSize + (grid.cellSize / 2);
					sy = y * grid.cellSize + (grid.cellSize / 2);
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
		startX = sx;
		startY = sy;
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

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public List<Wall> getWalls() {
		return walls;
	}

	public void draw(Graphics2D g) {
		if (game.state.debug) {
			for (Wall wall : walls) {
				wall.draw(g);
			}
			grid.draw(g);
		}
	}

}
