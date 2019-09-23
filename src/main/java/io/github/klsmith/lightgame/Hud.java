package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Hud {

    private final LightGame game;

    public Hud(LightGame game) {
        this.game = game;
    }

    public void draw(Graphics2D g) {
        final LightController lightController = game.getPlayer().getLight().getController();
        g.setColor(Color.RED);
        g.drawString("Spread: " + lightController.getSpread()
                + "\u00B0  Resolution: " + lightController.getResolution()
                + "  Range: " + lightController.getRange(), 8, 16);
    }

}
