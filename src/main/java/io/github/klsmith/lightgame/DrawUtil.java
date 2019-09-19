package io.github.klsmith.lightgame;

import java.awt.Graphics2D;

public class DrawUtil {

	public static void outlineCircle(Graphics2D g, int x, int y, int radius) {
		final int leftX = x - radius;
		final int topY = y - radius;
		final int diameter = radius * 2;
		g.drawOval(leftX, topY, diameter, diameter);
	}

	public static void fillCircle(Graphics2D g, int x, int y, int radius) {
		final int leftX = x - radius;
		final int topY = y - radius;
		final int diameter = radius * 2;
		g.fillOval(leftX, topY, diameter, diameter);
	}

}
