package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class LightGame extends JPanel {

	private static final String TITLE = "Light Game";

	final AppState state;
	final Mouse mouse;
	final Level level;
	final Player player;
	final View view;

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
		level = new Level(this, new Grid(32, 20, 15), new int[] {
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
		player = new Player(this);
		state = new AppState(this);
		mouse = new Mouse(this);
		view = new View(this);
		setPreferredSize(new Dimension(level.getWidth(), level.getHeight()));
	}

	private void update() {
		player.update();
		view.update();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		render((Graphics2D) g);
	}

	private synchronized void render(Graphics2D g) {
		g.scale(getWidth() / view.getWidth(), getHeight() / view.getHeight());
		g.translate(-view.getX(), -view.getY());
		level.draw(g);
		player.draw(g);
		view.draw(g);
	}

}
