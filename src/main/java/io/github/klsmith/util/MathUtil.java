package io.github.klsmith.util;

public final class MathUtil {

    private MathUtil() {}

    public static double pointDirection(double x1, double y1, double x2, double y2) {
        final double x = x2 - x1;
        final double y = y2 - y1;
        double angle = Math.toDegrees(Math.atan2(y, x));
        if (angle < 0) {
            angle += 360;
        }
        return Math.toRadians(angle);
    }

    public static double lengthDirX(double distance, double direction) {
        return distance * Math.cos(direction);
    }

    public static double lengthDirY(double distance, double direction) {
        return distance * Math.sin(direction);
    }

    private static double length(Double2 v) {
        return Math.sqrt((v.getX() * v.getX()) + (v.getY() * v.getY()));
    }

    public static double signedDstToCircle(Double2 p, Double2 center, double radius) {
        return length(center.subtract(p)) - radius;
    }

    public static double signedDstToBox(Double2 p, Double2 center, Double2 size) {
        final Double2 offset = p.subtract(center).abs().subtract(size);
        final double unsignedDst = length(offset.max(0));
        final double dstInsideBox = offset.min(0).max();
        return unsignedDst + dstInsideBox;
    }

}
