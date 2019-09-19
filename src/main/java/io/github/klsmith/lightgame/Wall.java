package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Wall {

	final int width;
	final int height;
	final boolean isCircle;
	int x = 0;
	int y = 0;

	public Wall(int size) {
		this(size, false);
	}

	public Wall(int size, boolean isCircle) {
		width = size;
		height = size;
		this.isCircle = isCircle;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		if (isCircle) {
			g.fillOval(x, y, width, height);
		} else {
			g.fillRect(x, y, width, height);
		}
	}

}
