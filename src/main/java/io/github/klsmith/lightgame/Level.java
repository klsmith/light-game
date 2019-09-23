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
        for (int y = 0; y < grid.getRows(); y++) {
            for (int x = 0; x < grid.getColumns(); x++) {
                final int index = (y * grid.getColumns()) + x;
                int marker = level[index];
                if (1 == marker) {
                    sx = x * grid.getCellSize() + (grid.getCellSize() / 2);
                    sy = y * grid.getCellSize() + (grid.getCellSize() / 2);
                }
                if (2 == marker || 3 == marker) {
                    final boolean isCircle = 3 == marker ? true : false;
                    final Wall wall = new Wall(grid.getCellSize(), isCircle);
                    wall.setX(x * grid.getCellSize());
                    wall.setY(y * grid.getCellSize());
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
        return grid.getColumns() * grid.getCellSize();
    }

    public int getHeight() {
        return grid.getRows() * grid.getCellSize();
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
        if (game.getStateController().debug()) {
            for (Wall wall : walls) {
                wall.draw(g);
            }
            grid.draw(g);
        }
    }

}
