package io.github.klsmith.lightgame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Mouse {

    private int x = 0;
    private int y = 0;

    private final LightGame game;
    private final Controller controller;

    public Mouse(LightGame game) {
        this.game = game;
        this.controller = new Controller();
        game.addMouseMotionListener(controller);
    }

    public int getWindowX() {
        return x;
    }

    public int getWindowY() {
        return y;
    }

    public int getGameX() {
        final double xScalingFactor = (double) game.getView().getWidth() / (double) game.getWidth();
        return (int) (getWindowX() * xScalingFactor) + game.getView().getX();
    }

    public int getGameY() {
        final double yScalingFactor = (double) game.getView().getHeight() / (double) game.getHeight();
        return (int) (getWindowY() * yScalingFactor) + game.getView().getY();
    }

    private class Controller implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

    }

}
