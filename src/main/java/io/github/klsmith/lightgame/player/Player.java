package io.github.klsmith.lightgame.player;

import java.awt.Color;
import java.awt.Graphics2D;

import io.github.klsmith.lightgame.DrawUtil;
import io.github.klsmith.lightgame.LightGame;

public class Player {

    private final int radius = 8;
    private final int speed = 2;
    private int x = 0;
    private int y = 0;

    private final LightGame game;
    private final Light light;
    private final PlayerController controller;

    public Player(LightGame game) {
        this.game = game;
        x = game.getLevel().getStartX();
        y = game.getLevel().getStartY();
        this.light = new Light(game);
        this.controller = new PlayerController(game);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update() {
        if (controller.up() && !controller.down()) {
            y -= speed;
        }
        if (controller.down() && !controller.up()) {
            y += speed;
        }
        if (controller.left() && !controller.right()) {
            x -= speed;
        }
        if (controller.right() && !controller.left()) {
            x += speed;
        }
        light.update();
    }

    public Light getLight() {
        return light;
    }

    public void draw(Graphics2D g) {
        light.draw(g);
        g.setColor(Color.RED);
        DrawUtil.fillCircle(g, x, y, radius);
        if (game.getStateController().debug()) {
            g.setColor(Color.GRAY);
            if (controller.up()) {
                g.drawRect(x - radius, y - (radius * 3), radius * 2, radius * 2);
            }
            if (controller.down()) {
                g.drawRect(x - radius, y + radius, radius * 2, radius * 2);
            }
            if (controller.left()) {
                g.drawRect(x - (radius * 3), y - radius, radius * 2, radius * 2);
            }
            if (controller.right()) {
                g.drawRect(x + radius, y - radius, radius * 2, radius * 2);
            }
        }
    }

}
