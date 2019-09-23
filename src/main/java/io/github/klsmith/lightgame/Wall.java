package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Wall {

    private final int width;
    private final int height;
    private final boolean isCircle;
    private int x = 0;
    private int y = 0;

    public Wall(int size) {
        this(size, false);
    }

    public Wall(int size, boolean isCircle) {
        width = size;
        height = size;
        this.isCircle = isCircle;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.GREEN);
        if (isCircle) {
            g.fillOval(x, y, width, height);
        } else {
            g.fillRect(x, y, width, height);
        }
    }

}
