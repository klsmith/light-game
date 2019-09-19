package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player {

	final int radius = 8;
	final int speed = 2;
	int x = 0;
	int y = 0;

	private final Controller controller;

	public Player() {
		controller = new Controller();
	}

	public void update() {
		if (controller.up && !controller.down) {
			y -= speed;
		}
		if (controller.down && !controller.up) {
			y += speed;
		}
		if (controller.left && !controller.right) {
			x -= speed;
		}
		if (controller.right && !controller.left) {
			x += speed;
		}
	}

	public Controller getController() {
		return controller;
	}

	public class Controller implements KeyListener {

		private boolean left = false;
		private boolean right = false;
		private boolean down = false;
		private boolean up = false;

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

		public void draw(Graphics2D g) {
			g.setColor(Color.GRAY);
			if (up) {
				g.drawRect(x - radius, y - (radius * 3), radius * 2, radius * 2);
			}
			if (down) {
				g.drawRect(x - radius, y + radius, radius * 2, radius * 2);
			}
			if (left) {
				g.drawRect(x - (radius * 3), y - radius, radius * 2, radius * 2);
			}
			if (right) {
				g.drawRect(x + radius, y - radius, radius * 2, radius * 2);
			}
		}

	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		DrawUtil.fillCircle(g, x, y, radius);
	}

}
