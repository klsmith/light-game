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
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class LightGame extends JPanel implements KeyListener, MouseMotionListener {

	private boolean running = true;
	private boolean debug = false;
	private boolean infrared = false;

	private final Mouse mouse = new Mouse();
	private final LightSettings light = new LightSettings(45, 45);
	private final GridSettings grid = new GridSettings(32, 20, 15);
	private final Player player = new Player();
	private final List<Wall> walls = new ArrayList<>();

	public static void main(String[] args) {
		SwingUtilities.invokeLater(LightGame::startGameWindow);
	}

	public static void startGameWindow() {
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
		final int panelWidth = grid.columns * grid.cellSize;
		final int panelHeight = grid.rows * grid.cellSize;
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		addKeyListener(this);
		addMouseMotionListener(this);
	}

	private void readLevel() {
		final int[] level = {
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
		for (int y = 0; y < grid.rows; y++) {
			for (int x = 0; x < grid.columns; x++) {
				final int index = (y * grid.columns) + x;
				int marker = level[index];
				if (1 == marker) {
					player.x = x * grid.cellSize + (grid.cellSize / 2);
					player.y = y * grid.cellSize + (grid.cellSize / 2);
				}
				if (2 == marker || 3 == marker) {
					final boolean isCircle = 3 == marker ? true : false;
					final Wall wall = new Wall(grid.cellSize, isCircle);
					wall.x = x * grid.cellSize;
					wall.y = y * grid.cellSize;
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

	private static final int BACKGROUND = 0;
	private static final int PLAYER = 1;
	private static final int FOREGROUND = 2;

	private final List<List<Consumer<Graphics2D>>> layers = new ArrayList<>();

	private void resetLayers() {
		layers.clear();
		layers.add(new ArrayList<>());
		layers.add(new ArrayList<>());
		layers.add(new ArrayList<>());
	}

	private void renderLayers(Graphics2D g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		for (List<Consumer<Graphics2D>> list : layers) {
			for (Consumer<Graphics2D> drawer : list) {
				try {
					drawer.accept(g);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected synchronized void render(Graphics2D graphics) {
		resetLayers();
		layers.get(BACKGROUND).add(g -> {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
		});
		if (debug) {
			layers.get(FOREGROUND).add(g -> {
				g.setColor(Color.LIGHT_GRAY);
				for (int x = 0; x < getWidth(); x += grid.cellSize) {
					g.drawLine(x, 0, x, getHeight());
				}
				for (int y = 0; y < getHeight(); y += grid.cellSize) {
					g.drawLine(0, y, getWidth(), y);
				}
			});
		}
		movePlayer();
		if (debug) {
			layers.get(PLAYER).add(g -> {
				g.setColor(Color.GRAY);
				if (player.up) {
					g.drawRect(player.x - player.radius, player.y - (player.radius * 3), player.radius * 2,
							player.radius * 2);
				}
				if (player.down) {
					g.drawRect(player.x - player.radius, player.y + player.radius, player.radius * 2,
							player.radius * 2);
				}
				if (player.left) {
					g.drawRect(player.x - (player.radius * 3), player.y - player.radius, player.radius * 2,
							player.radius * 2);
				}
				if (player.right) {
					g.drawRect(player.x + player.radius, player.y - player.radius, player.radius * 2,
							player.radius * 2);
				}
			});
		}
		drawLight(graphics, light.spread, light.resolution);
		if (debug) {
			layers.get(BACKGROUND).add(g -> {
				for (Wall wall : walls) {
					g.setColor(Color.GREEN);
					if (wall.isCircle) {
						g.fillOval(wall.x, wall.y, wall.width, wall.height);
					} else {
						g.fillRect(wall.x, wall.y, wall.width, wall.height);
					}
				}
			});
		}
		layers.get(PLAYER).add(g -> {
			g.setColor(Color.RED);
			fillCircle(g, player.x, player.y, player.radius);
		});
		layers.get(FOREGROUND).add(g -> {
			g.setColor(Color.WHITE);
			g.drawString("Spread: " + light.spread + "\u00B0  Resolution: " + light.resolution, 8, 16);
		});
		renderLayers(graphics);
	}

	private void drawLight(Graphics2D graphics, double degreeSpread, int resolution) {
		final double mouseDirection = getDirectionFromPlayerToMouse();
		final double halfSpread = Math.toRadians(degreeSpread / 2);
		final double offset = Math.toRadians(degreeSpread / resolution);
		int[] lightX = new int[resolution + 1];
		int[] lightY = new int[resolution + 1];
		lightX[0] = player.x;
		lightY[0] = player.y;
		for (int i = 0; i < resolution; i++) {
			final double direction = mouseDirection - halfSpread + (i * offset);
			final Double2 point = drawMarchingCircles(graphics, player.x, player.y, direction);
			lightX[i + 1] = (int) point.x;
			lightY[i + 1] = (int) point.y;
		}
		if (infrared) {
			layers.get(PLAYER).add(g -> {
				g.setColor(Color.RED);
				g.drawPolygon(lightX, lightY, resolution + 1);
			});
		} else {
			layers.get(PLAYER).add(g -> {
				g.setColor(Color.WHITE);
				g.fillPolygon(lightX, lightY, resolution + 1);
			});
		}
	}

	private Double2 drawMarchingCircles(Graphics2D graphics, double x, double y, double direction) {
		final double shortestDistance = shortestDistanceFromPointToWall(x, y);
		final double dirX = x + lengthDirX(shortestDistance, direction);
		final double dirY = y + lengthDirY(shortestDistance, direction);
		if (debug) {
			layers.get(FOREGROUND).add(g -> {
				g.setColor(Color.GRAY);
				outlineCircle(g, (int) x, (int) y, (int) shortestDistance);
				fillCircle(g, (int) dirX, (int) dirY, 3);
			});
		}
		if (shortestDistance >= 1.0) {
			return drawMarchingCircles(graphics, dirX, dirY, direction);
		}
		if (debug) {
			layers.get(FOREGROUND).add(g -> {
				g.setColor(Color.GREEN);
				fillCircle(g, (int) x, (int) y, 1);
			});
		}
		final Double2 result = new Double2();
		result.x = x;
		result.y = y;
		return result;
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
		return pointDirection(player.x, player.y, mouse.x, mouse.y);
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

	private void movePlayer() {
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
		case KeyEvent.VK_M:
			infrared = !infrared;
			break;
		case KeyEvent.VK_O:
			light.spread--;
			break;
		case KeyEvent.VK_P:
			if (light.spread < 360) {
				light.spread++;
			}
			break;
		case KeyEvent.VK_9:
			if (light.resolution > 1) {
				light.resolution--;
			}
			break;
		case KeyEvent.VK_0:
			light.resolution++;
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
		mouse.x = e.getX();
		mouse.y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse.x = e.getX();
		mouse.y = e.getY();
	}

}
