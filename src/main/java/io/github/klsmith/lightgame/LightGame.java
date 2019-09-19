package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class LightGame extends JPanel implements KeyListener {

	private boolean running = true;
	private boolean debug = false;

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
		addKeyListener(player.getController());
		addKeyListener(light.getController());
		addMouseMotionListener(mouse.getController());
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
		player.update();
		resetLayers();
		layers.get(BACKGROUND).add(g -> {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
		});
		if (debug) {
			for (Wall wall : walls) {
				layers.get(BACKGROUND).add(wall::draw);
			}
		}
		if (debug) {
			layers.get(PLAYER).add(player.getController()::draw);
		}
		drawLight(graphics, light.spread, light.resolution);
		layers.get(PLAYER).add(player::draw);
		if (debug) {
			layers.get(FOREGROUND).add(grid::draw);
		}
		layers.get(FOREGROUND).add(light::draw);
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
		if (light.infrared) {
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
		final double dirX = x + MathUtil.lengthDirX(shortestDistance, direction);
		final double dirY = y + MathUtil.lengthDirY(shortestDistance, direction);
		if (debug) {
			layers.get(FOREGROUND).add(g -> {
				g.setColor(Color.GRAY);
				DrawUtil.outlineCircle(g, (int) x, (int) y, (int) shortestDistance);
				DrawUtil.fillCircle(g, (int) dirX, (int) dirY, 3);
			});
		}
		if (shortestDistance >= 1.0) {
			return drawMarchingCircles(graphics, dirX, dirY, direction);
		}
		if (debug) {
			layers.get(FOREGROUND).add(g -> {
				g.setColor(Color.GREEN);
				DrawUtil.fillCircle(g, (int) x, (int) y, 1);
			});
		}
		final Double2 result = new Double2();
		result.x = x;
		result.y = y;
		return result;
	}

	private double getDirectionFromPlayerToMouse() {
		return MathUtil.pointDirection(player.x, player.y, mouse.x, mouse.y);
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
				distanceToOutside = MathUtil.signedDstToCircle(point, wallCenter, wallSize.x);
			} else {
				distanceToOutside = MathUtil.signedDstToBox(point, wallCenter, wallSize);
			}
			result = Math.min(result, distanceToOutside);
		}
		return result;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			debug = !debug;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
