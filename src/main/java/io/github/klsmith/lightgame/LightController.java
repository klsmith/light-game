package io.github.klsmith.lightgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import io.github.klsmith.util.MathUtil;

public class LightController implements KeyListener {

    private int spread = 45;
    private int resolution = 45;
    private int range = 256;

    private boolean infrared = false;

    private final LightGame game;

    public LightController(LightGame game) {
        this.game = game;
        game.addKeyListener(this);
    }

    public int getSpread() {
        return spread;
    }

    public int getResolution() {
        return resolution;
    }

    public int getRange() {
        return range;
    }

    public double getDirection() {
        return MathUtil.pointDirection(game.getPlayer().getX(), game.getPlayer().getY(),
                game.getMouse().getGameX(), game.getMouse().getGameY());
    }

    public boolean infrared() {
        return infrared;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_M:
                infrared = !infrared;
                break;
            case KeyEvent.VK_O:
                spread--;
                break;
            case KeyEvent.VK_P:
                if (spread < 360) {
                    spread++;
                }
                break;
            case KeyEvent.VK_9:
                if (resolution > 1) {
                    resolution--;
                }
                break;
            case KeyEvent.VK_0:
                resolution++;
                break;
            case KeyEvent.VK_SEMICOLON:
                range--;
                break;
            case KeyEvent.VK_QUOTE:
                range++;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
