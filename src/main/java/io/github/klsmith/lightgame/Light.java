package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Light {

	int spread;
	int resolution;
	boolean infrared;

	private final Controller controller;

	public Light(int spread, int resolution) {
		this.spread = spread;
		this.resolution = resolution;
		this.infrared = false;
		this.controller = new Controller();
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawString("Spread: " + spread + "\u00B0  Resolution: " + resolution, 8, 16);
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
