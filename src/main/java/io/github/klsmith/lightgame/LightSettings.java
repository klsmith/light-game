package io.github.klsmith.lightgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LightSettings {

	int spread;
	int resolution;
	boolean infrared;

	private final Controller controller;

	public LightSettings(int spread, int resolution) {
		this.spread = spread;
		this.resolution = resolution;
		this.infrared = false;
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
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

	}

}
