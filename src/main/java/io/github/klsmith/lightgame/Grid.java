package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Grid {

    private final int cellSize;
    private final int columns;
    private final int rows;

    public Grid(int cellSize, int columns, int rows) {
        this.cellSize = cellSize;
        this.columns = columns;
        this.rows = rows;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
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
