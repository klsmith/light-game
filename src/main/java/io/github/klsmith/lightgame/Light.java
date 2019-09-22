package io.github.klsmith.lightgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import io.github.klsmith.util.Double2;
import io.github.klsmith.util.MathUtil;

public class Light {

    private int spread;
    private int resolution;
    private boolean infrared;
    private final int range = 256;

    private Polygon shape;

    private final LightGame game;
    private final List<Point> debugDots;
    private final Controller controller;
    private double direction = 0;

    public Light(LightGame game, int spread, int resolution) {
        this.game = game;
        this.spread = spread;
        this.resolution = resolution;
        this.infrared = false;
        this.controller = new Controller();
        game.addKeyListener(controller);
        this.shape = null;
        this.debugDots = new ArrayList<>();
    }

    public int getSpread() {
        return spread;
    }

    public int getResolution() {
        return resolution;
    }

    private synchronized void incrementResolution() {
        resolution++;
    }

    private synchronized void decrementResolution() {
        resolution--;
    }

    public synchronized void update() {
        debugDots.clear();
        direction = getDirectionFromPlayerToMouse();
        final double halfSpread = Math.toRadians(spread / 2);
        final double offset = Math.toRadians(spread) / resolution;
        int[] lightX = new int[resolution + 1];
        int[] lightY = new int[resolution + 1];
        lightX[0] = game.player.x;
        lightY[0] = game.player.y;
        for (int i = 0; i < resolution; i++) {
            final double rayDirection = direction - halfSpread + (i * offset);
            final Double2 point = drawMarchingCircles(game.player.x, game.player.y, rayDirection, 0);
            lightX[i + 1] = (int) point.getX();
            lightY[i + 1] = (int) point.getY();
        }
        shape = new Polygon(lightX, lightY, resolution + 1);
    }

    private Double2 drawMarchingCircles(double x, double y, double radians, double totalDistance) {
        final double shortestDistance = Math.min(range - totalDistance, shortestDistanceFromPointToWall(x, y));
        final double dirX = x + MathUtil.lengthDirX(shortestDistance, radians);
        final double dirY = y + MathUtil.lengthDirY(shortestDistance, radians);
        if (game.state.debug) {
            debugDots.add(new Point((int) x, (int) y));
        }
        if (shortestDistance >= 1.0) {
            return drawMarchingCircles(dirX, dirY, radians, totalDistance + shortestDistance);
        }
        return new Double2(x, y);
    }

    private double getDirectionFromPlayerToMouse() {
        return MathUtil.pointDirection(game.player.x, game.player.y,
                game.mouse.getGameX(), game.mouse.getGameY());
    }

    private double shortestDistanceFromPointToWall(double x, double y) {
        final Double2 point = new Double2(x, y);
        double result = Double.MAX_VALUE;
        for (Wall wall : game.level.getWalls()) {
            final double wallCenterX = wall.x + (wall.width / 2);
            final double wallCenterY = wall.y + (wall.height / 2);
            final Double2 wallCenter = new Double2(wallCenterX, wallCenterY);
            final double wallSizeX = wall.width / 2;
            final double wallSizeY = wall.height / 2;
            final Double2 wallSize = new Double2(wallSizeX, wallSizeY);
            final double distanceToOutside;
            if (wall.isCircle) {
                distanceToOutside = MathUtil.signedDstToCircle(point, wallCenter, wallSize.getX());
            } else {
                distanceToOutside = MathUtil.signedDstToBox(point, wallCenter, wallSize);
            }
            result = Math.min(result, distanceToOutside);
        }
        return result;
    }

    public synchronized void draw(Graphics2D g) {
        if (null != shape) {
            if (infrared) {
                g.setColor(Color.RED);
                g.draw(shape);
            } else {
                final RadialGradientPaint radialGradient = new RadialGradientPaint(
                        game.player.x, game.player.y, range,
                        new float[] { 0, 1 },
                        new Color[] { Color.WHITE, new Color(255, 255, 255, 0) });
                g.setPaint(radialGradient);
                g.fill(shape);
            }
        }
        for (Point point : debugDots) {
            if (infrared) {
                g.setColor(Color.WHITE);
                DrawUtil.fillCircle(g, point.x, point.y, 1);
            } else {
                g.setColor(Color.BLACK);
                DrawUtil.fillCircle(g, point.x, point.y, 1);
            }
        }
    }

    private class Controller implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

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
        public void keyReleased(KeyEvent e) {}

    }

}
