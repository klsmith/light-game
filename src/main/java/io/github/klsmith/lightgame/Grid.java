package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Grid {

	final int cellSize;
	final int columns;
	final int rows;

	public Grid(int cellSize, int columns, int rows) {
		this.cellSize = cellSize;
		this.columns = columns;
		this.rows = rows;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		final int totalWidth = columns * cellSize;
		final int totalHeight = rows * cellSize;
		for (int x = 0; x < totalWidth; x += cellSize) {
			g.drawLine(x, 0, x, totalHeight);
		}
		for (int y = 0; y < totalHeight; y += cellSize) {
			g.drawLine(0, y, totalWidth, y);
		}
	}

}
