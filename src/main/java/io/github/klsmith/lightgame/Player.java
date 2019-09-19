package io.github.klsmith.lightgame;

public class Player {

	final int radius = 8;
	final int speed = 2;
	int x = 0;
	int y = 0;
	boolean left = false;
	boolean right = false;
	boolean down = false;
	boolean up = false;

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

}
