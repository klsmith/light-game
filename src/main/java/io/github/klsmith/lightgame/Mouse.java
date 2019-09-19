package io.github.klsmith.lightgame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Mouse {

	int x = 0;
	int y = 0;

	private final Controller controller;

	public Mouse() {
		this.controller = new Controller();
	}

	public Controller getController() {
		return controller;
	}

	public class Controller implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			x = e.getX();
			y = e.getY();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			x = e.getX();
			y = e.getY();
		}

	}

}
