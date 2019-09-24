package io.github.klsmith.lightgame.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import io.github.klsmith.lightgame.LightGame;

public class PlayerController implements KeyListener {

    private boolean left = false;
    private boolean right = false;
    private boolean down = false;
    private boolean up = false;

    public PlayerController(LightGame game) {
        game.addKeyListener(this);
    }

    public boolean left() {
        return left;
    }

    public boolean right() {
        return right;
    }

    public boolean down() {
        return down;
    }

    public boolean up() {
        return up;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
        }
    }

}
