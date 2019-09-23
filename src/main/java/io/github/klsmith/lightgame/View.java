package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;

public class View {

    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int height = 0;

    private final LightGame game;

    public View(LightGame game) {
        this.game = game;
        this.width = game.getLevel().getWidth();
        this.height = game.getLevel().getHeight();
    }

    public void update() {
        x = game.getPlayer().getX() - (width / 2);
        y = game.getPlayer().getY() - (height / 2);
    }

    public void draw(Graphics2D g) {
        if (game.getStateController().debug()) {
            g.setColor(Color.GREEN);
            g.drawRect(x, y, width, height);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
