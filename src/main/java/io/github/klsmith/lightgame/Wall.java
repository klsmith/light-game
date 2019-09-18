package io.github.klsmith.lightgame;

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

}
