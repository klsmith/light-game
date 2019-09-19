package io.github.klsmith.lightgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player {

	final int radius = 8;
	final int speed = 2;
	int x = 0;
	int y = 0;
	boolean left = false;
	boolean right = false;
	boolean down = false;
	boolean up = false;

	private final Controller controller;

	public Player() {
		controller = new Controller();
	}

	public void update() {
		if (up && !down) {
			y -= speed;
		}
		if (down && !up) {
			y += speed;
		}
		if (left && !right) {
			x -= speed;
		}
		if (right && !left) {
			x += speed;
		}
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

}
