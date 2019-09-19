package io.github.klsmith.lightgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AppState {

	boolean running = true;
	boolean debug = false;

	private final Controller controller;

	public AppState() {
		this.controller = new Controller();
	}

	public Controller getController() {
		return controller;
	}

	public class Controller implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				debug = !debug;
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

	}

}
