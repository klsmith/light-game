package io.github.klsmith.lightgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class StateController implements KeyListener {

    private boolean running = true;
    private boolean debug = false;

    public StateController(LightGame game) {
        game.addKeyListener(this);
    }

    public boolean running() {
        return running;
    }

    public boolean debug() {
        return debug;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                debug = !debug;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
