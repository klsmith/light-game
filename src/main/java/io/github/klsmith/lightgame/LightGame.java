package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class LightGame extends JPanel implements KeyListener, MouseMotionListener {

	private boolean debug = false;

	private final int gridCellSize = 32;
	private final int gridWidth = 20;
	private final int gridHeight = 15;

	private boolean running = true;

	private final int[] level = {
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, //
			2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, //
			2, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, //
			2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, //
			2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, //
			2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, //
			2, 0, 0, 0, 0, 1, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2, //
			2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, //
			2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, //
			2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, //
			2, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, //
			2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, //
			2, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, //
			2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, //
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 //
	};

	private final List<Wall> walls = new ArrayList<>();

	private int mouseX = 0;
	private int mouseY = 0;

	private final Player player = new Player();

	private class Player {

		private final int radius = 8;
		private final int speed = 2;
		private int x = 0;
		private int y = 0;
		private boolean left = false;
		private boolean right = false;
		private boolean down = false;
		private boolean up = false;

	}

	private class Wall {

		private boolean isCircle = false;
		private final int width = gridCellSize;
		private final int height = gridCellSize;
		private int x = 0;
		private int y = 0;

	}

	private static class Double2 {

		double x;
		double y;

		Double2 subtract(Double2 other) {
			final Double2 result = new Double2();
			result.x = x - other.x;
			result.y = y - other.y;
			return result;
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(LightGame::showWindow);
	}

	public static void showWindow() {
		final JFrame frame = new JFrame("Light Game");
		final LightGame game = new LightGame();
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.grabFocus();
		final Thread gameLoop = new Thread() {

			@Override
			public void run() {
				while (game.running) {
					game.paintImmediately(0, 0, game.getWidth(), game.getHeight());
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		};
		gameLoop.start();
	}

	public LightGame() {
		readLevel();
		final int panelWidth = gridWidth * gridCellSize;
		final int panelHeight = gridHeight * gridCellSize;
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		addKeyListener(this);
		addMouseMotionListener(this);
	}

	private void readLevel() {
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				final int index = (y * gridWidth) + x;
				int marker = level[index];
				if (1 == marker) {
					player.x = x * gridCellSize + (gridCellSize / 2);
					player.y = y * gridCellSize + (gridCellSize / 2);
				}
				if (2 == marker || 3 == marker) {
					final Wall wall = new Wall();
					wall.x = x * gridCellSize;
					wall.y = y * gridCellSize;
					if (3 == marker) {
						wall.isCircle = true;
					}
					walls.add(wall);
				}
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
	}

	protected void render(Graphics2D g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (debug) {
			g.setColor(Color.LIGHT_GRAY);
			for (int x = 0; x < getWidth(); x += gridCellSize) {
				g.drawLine(x, 0, x, getHeight());
			}
			for (int y = 0; y < getHeight(); y += gridCellSize) {
				g.drawLine(0, y, getWidth(), y);
			}
		}
		for (Wall wall : walls) {
			g.setColor(Color.BLACK);
			if (wall.isCircle) {
				g.fillOval(wall.x, wall.y, wall.width, wall.height);
			} else {
				g.fillRect(wall.x, wall.y, wall.width, wall.height);
			}
			if (debug) {
				g.setColor(Color.GREEN);
				final int centerX = wall.x + (wall.width / 2);
				final int centerY = wall.y + (wall.height / 2);
				fillCircle(g, centerX, centerY, 1);
			}
		}
		movePlayer(g);
		g.setColor(Color.RED);
		fillCircle(g, player.x, player.y, player.radius);
		final double mouseDirection = getDirectionFromPlayerToMouse();
		g.setColor(Color.GRAY);
		drawMarchingCircles(g, player.x, player.y, mouseDirection);
	}

	private void drawMarchingCircles(Graphics2D g, double x, double y, double direction) {
		final double shortestDistance = shortestDistanceFromPointToWall(x, y);
		outlineCircle(g, (int) x, (int) y, (int) shortestDistance);
		final double dirX = x + lengthDirX(shortestDistance, direction);
		final double dirY = y + lengthDirY(shortestDistance, direction);
		fillCircle(g, (int) dirX, (int) dirY, 3);
		if (shortestDistance >= 1.0) {
			drawMarchingCircles(g, dirX, dirY, direction);
		} else {
			final int size = 8;
			g.setColor(Color.RED);
			g.drawLine((int) x - size, (int) y - size, (int) x + size, (int) y + size);
			g.drawLine((int) x - size, (int) y + size, (int) x + size, (int) y - size);
		}
	}

	private void outlineCircle(Graphics2D g, int x, int y, int radius) {
		final int leftX = x - radius;
		final int topY = y - radius;
		final int diameter = radius * 2;
		g.drawOval(leftX, topY, diameter, diameter);
	}

	private void fillCircle(Graphics2D g, int x, int y, int radius) {
		final int leftX = x - radius;
		final int topY = y - radius;
		final int diameter = radius * 2;
		g.fillOval(leftX, topY, diameter, diameter);
	}

	private double getDirectionFromPlayerToMouse() {
		return pointDirection(player.x, player.y, mouseX, mouseY);
	}

	private double shortestDistanceFromPointToWall(double x, double y) {
		final Double2 point = new Double2();
		point.x = x;
		point.y = y;
		double result = Double.MAX_VALUE;
		for (Wall wall : walls) {
			final Double2 wallCenter = new Double2();
			wallCenter.x = wall.x + (wall.width / 2);
			wallCenter.y = wall.y + (wall.height / 2);
			final Double2 wallSize = new Double2();
			wallSize.x = wall.width / 2;
			wallSize.y = wall.height / 2;
			final double distanceToOutside;
			if (wall.isCircle) {
				distanceToOutside = signedDstToCircle(point, wallCenter, wallSize.x);
			} else {
				distanceToOutside = signedDstToBox(point, wallCenter, wallSize);
			}
			result = Math.min(result, distanceToOutside);
		}
		return result;
	}

	private void movePlayer(Graphics2D g) {
		if (player.up && !player.down) {
			player.y -= player.speed;
		}
		if (player.down && !player.up) {
			player.y += player.speed;
		}
		if (player.left && !player.right) {
			player.x -= player.speed;
		}
		if (player.right && !player.left) {
			player.x += player.speed;
		}
		if (debug) {
			g.setColor(Color.GRAY);
			if (player.up) {
				g.drawRect(player.x - player.radius, player.y - (player.radius * 3), player.radius * 2,
						player.radius * 2);
			}
			if (player.down) {
				g.drawRect(player.x - player.radius, player.y + player.radius, player.radius * 2, player.radius * 2);
			}
			if (player.left) {
				g.drawRect(player.x - (player.radius * 3), player.y - player.radius, player.radius * 2,
						player.radius * 2);
			}
			if (player.right) {
				g.drawRect(player.x + player.radius, player.y - player.radius, player.radius * 2, player.radius * 2);
			}
		}
	}

	private double pointDirection(double x1, double y1, double x2, double y2) {
		final double x = x2 - x1;
		final double y = y2 - y1;
		double angle = Math.toDegrees(Math.atan2(y, x));
		if (angle < 0) {
			angle += 360;
		}
		return Math.toRadians(angle);
	}

	private double lengthDirX(double distance, double direction) {
		return distance * Math.cos(direction);
	}

	private double lengthDirY(double distance, double direction) {
		return distance * Math.sin(direction);
	}

	private double length(Double2 v) {
		return Math.sqrt((v.x * v.x) + (v.y * v.y));
	}

	private double signedDstToCircle(Double2 p, Double2 center, double radius) {
		return length(center.subtract(p)) - radius;
	}

	private double signedDstToBox(Double2 p, Double2 center, Double2 size) {
		final Double2 offset = abs(p.subtract(center)).subtract(size);
		final double unsignedDst = length(max(offset, 0));
		final double dstInsideBox = max(min(offset, 0));
		return unsignedDst + dstInsideBox;
	}

	private Double2 abs(Double2 v) {
		final Double2 result = new Double2();
		result.x = Math.abs(v.x);
		result.y = Math.abs(v.y);
		return result;
	}

	private Double2 max(Double2 v, double value) {
		final Double2 result = new Double2();
		result.x = Math.max(v.x, value);
		result.y = Math.max(v.y, value);
		return result;
	}

	private Double2 min(Double2 v, double value) {
		final Double2 result = new Double2();
		result.x = Math.min(v.x, value);
		result.y = Math.min(v.y, value);
		return result;
	}

	private double max(Double2 v) {
		return Math.max(v.x, v.y);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.up = true;
			break;
		case KeyEvent.VK_S:
			player.down = true;
			break;
		case KeyEvent.VK_A:
			player.left = true;
			break;
		case KeyEvent.VK_D:
			player.right = true;
			break;
		case KeyEvent.VK_SPACE:
			debug = !debug;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.up = false;
			break;
		case KeyEvent.VK_S:
			player.down = false;
			break;
		case KeyEvent.VK_A:
			player.left = false;
			break;
		case KeyEvent.VK_D:
			player.right = false;
			break;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

}
