package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class LightGame extends JPanel {

	private static final String TITLE = "Light Game";
	private static final int BACKGROUND = 0;
	private static final int PLAYER = 1;
	private static final int FOREGROUND = 2;

	private final AppState state;
	private final Mouse mouse;
	private final Light light;
	private final Player player;
	private final Level level;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(LightGame::startGameWindow);
	}

	public static void startGameWindow() {
		final JFrame frame = new JFrame(TITLE);
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
				while (game.state.running) {
					game.update();
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
		state = new AppState();
		mouse = new Mouse();
		light = new Light(45, 45);
		player = new Player();
		level = new Level(new Grid(32, 20, 15), new int[] {
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
		});
		level.resetPlayer(player);
		setPreferredSize(new Dimension(level.getWidth(), level.getHeight()));
		addKeyListener(player.getController());
		addKeyListener(light.getController());
		addKeyListener(state.getController());
		addMouseMotionListener(mouse.getController());
	}

	private void update() {
		player.update();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
	}

	private final List<List<Consumer<Graphics2D>>> layers = new ArrayList<>();

	private void resetLayers() {
		layers.clear();
		layers.add(new ArrayList<>());
		layers.add(new ArrayList<>());
		layers.add(new ArrayList<>());
	}

	private void renderLayers(Graphics2D g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
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
		if (state.debug) {
			for (Wall wall : level.getWalls()) {
				layers.get(BACKGROUND).add(wall::draw);
			}
		}
		if (state.debug) {
			layers.get(PLAYER).add(player.getController()::draw);
		}
		final double mouseDirection = getDirectionFromPlayerToMouse();
		drawLight(player.x, player.y, mouseDirection, light.spread, light.resolution);
		layers.get(PLAYER).add(player::draw);
		if (state.debug) {
			layers.get(FOREGROUND).add(level.getGridSettings()::draw);
		}
		layers.get(FOREGROUND).add(light::draw);
		renderLayers(graphics);
	}

	private void drawLight(int x, int y, double direction, double spreadInDegrees, int resolution) {
		final double halfSpread = spreadInDegrees / 2;
		final double offset = spreadInDegrees / resolution;
		int[] lightX = new int[resolution + 1];
		int[] lightY = new int[resolution + 1];
		lightX[0] = x;
		lightY[0] = y;
		for (int i = 0; i < resolution; i++) {
			final double rayDirection = Math.toRadians(direction - halfSpread + (i * offset));
			final Double2 point = drawMarchingCircles(x, y, rayDirection);
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

	private Double2 drawMarchingCircles(double x, double y, double radians) {
		final double shortestDistance = shortestDistanceFromPointToWall(x, y);
		final double dirX = x + MathUtil.lengthDirX(shortestDistance, radians);
		final double dirY = y + MathUtil.lengthDirY(shortestDistance, radians);
		if (state.debug) {
			layers.get(FOREGROUND).add(g -> {
				g.setColor(Color.GRAY);
				DrawUtil.outlineCircle(g, (int) x, (int) y, (int) shortestDistance);
				DrawUtil.fillCircle(g, (int) dirX, (int) dirY, 3);
			});
		}
		if (shortestDistance >= 1.0) {
			return drawMarchingCircles(dirX, dirY, radians);
		}
		if (state.debug) {
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

}
