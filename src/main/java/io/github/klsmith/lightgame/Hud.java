package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Hud {

    private final LightGame game;

    public Hud(LightGame game) {
        this.game = game;
    }

    public void draw(Graphics2D g) {
        final Light light = game.player.getLight();
        g.setColor(Color.RED);
        g.drawString("Spread: " + light.getSpread()
                + "\u00B0  Resolution: " + light.getResolution()
                + "  Range: " + light.getRange(), 8, 16);
    }

}
