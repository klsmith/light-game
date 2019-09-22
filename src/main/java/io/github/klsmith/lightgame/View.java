package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;

public class View {

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;

	private final LightGame game;

	public View(LightGame game) {
		this.game = game;
		this.width = game.level.getWidth();
		this.height = game.level.getHeight();
	}

	public void update() {
		x = game.player.x - (width / 2);
		y = game.player.y - (height / 2);
	}

	public void draw(Graphics2D g) {
		if (game.state.debug) {
			g.setColor(Color.GREEN);
			g.drawRect(x, y, width, height);
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
