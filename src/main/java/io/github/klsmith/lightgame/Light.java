package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Light {

	private int spread;
	private int resolution;
	private boolean infrared;

	private final AppState state;
	private final Level level;
	private final Mouse mouse;

	private Polygon shape;

	private List<Point> debugDots;

	private final Controller controller;

	public Light(LightGame game, int spread, int resolution) {
		this.state = game.state;
		this.level = game.level;
		this.mouse = game.mouse;
		this.spread = spread;
		this.resolution = resolution;
		this.infrared = false;
		this.controller = new Controller();
		this.shape = null;
		this.debugDots = new ArrayList<>();
	}

	public synchronized void incrementResolution() {
		resolution++;
	}

	public synchronized void decrementResolution() {
		resolution--;
	}

	public synchronized void update() {
		debugDots.clear();
		final double direction = getDirectionFromPlayerToMouse();
		final double halfSpread = Math.toRadians(spread / 2);
		final double offset = Math.toRadians(spread) / resolution;
		int[] lightX = new int[resolution + 1];
		int[] lightY = new int[resolution + 1];
		lightX[0] = level.getPlayer().x;
		lightY[0] = level.getPlayer().y;
		for (int i = 0; i < resolution; i++) {
			final double rayDirection = direction - halfSpread + (i * offset);
			final Double2 point = drawMarchingCircles(level.getPlayer().x, level.getPlayer().y, rayDirection);
			lightX[i + 1] = (int) point.x;
			lightY[i + 1] = (int) point.y;
		}
		shape = new Polygon(lightX, lightY, resolution + 1);
	}

	private Double2 drawMarchingCircles(double x, double y, double radians) {
		final double shortestDistance = shortestDistanceFromPointToWall(x, y);
		final double dirX = x + MathUtil.lengthDirX(shortestDistance, radians);
		final double dirY = y + MathUtil.lengthDirY(shortestDistance, radians);
		if (state.debug) {
			debugDots.add(new Point((int) x, (int) y));
		}
		if (shortestDistance >= 1.0) {
			return drawMarchingCircles(dirX, dirY, radians);
		}
		final Double2 result = new Double2();
		result.x = x;
		result.y = y;
		return result;
	}

	private double getDirectionFromPlayerToMouse() {
		return MathUtil.pointDirection(level.getPlayer().x, level.getPlayer().y, mouse.x, mouse.y);
	}

	private double shortestDistanceFromPointToWall(double x, double y) {
		final Double2 point = new Double2();
		point.x = x;
		point.y = y;
		double result = Double.MAX_VALUE;
		for (Wall wall : level.getWalls()) {
			final Double2 wallCenter = new Double2();
			wallCenter.x = wall.x + (wall.width / 2);
			wallCenter.y = wall.y + (wall.height / 2);
			final Double2 wallSize = new Double2();
			wallSize.x = wall.width / 2;
			wallSize.y = wall.height / 2;
			final double distanceToOutside;
			if (wall.isCircle) {
				distanceToOutside = MathUtil.signedDstToCircle(point, wallCenter, wallSize.x);
			} else {
				distanceToOutside = MathUtil.signedDstToBox(point, wallCenter, wallSize);
			}
			result = Math.min(result, distanceToOutside);
		}
		return result;
	}

	public synchronized void draw(Graphics2D g) {
		if (null != shape) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.RED);
		}
		g.drawString("Spread: " + spread + "\u00B0  Resolution: " + resolution, 8, 16);
		if (null != shape) {
			if (infrared) {
				g.setColor(Color.RED);
				g.draw(shape);
			} else {
				g.setColor(Color.WHITE);
				g.fill(shape);
			}
		}
		for (Point point : debugDots) {
			g.setColor(Color.BLACK);
			DrawUtil.fillCircle(g, point.x, point.y, 2);
			g.setColor(Color.WHITE);
			DrawUtil.fillCircle(g, point.x, point.y, 1);
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
					decrementResolution();
				}
				break;
			case KeyEvent.VK_0:
				incrementResolution();
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

	}

}
