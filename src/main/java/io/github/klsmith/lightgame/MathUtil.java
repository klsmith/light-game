package io.github.klsmith.lightgame;

public final class MathUtil {

	private MathUtil() {
	}

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
		return Math.sqrt((v.x * v.x) + (v.y * v.y));
	}

	public static double signedDstToCircle(Double2 p, Double2 center, double radius) {
		return length(center.subtract(p)) - radius;
	}

	public static double signedDstToBox(Double2 p, Double2 center, Double2 size) {
		final Double2 offset = abs(p.subtract(center)).subtract(size);
		final double unsignedDst = length(max(offset, 0));
		final double dstInsideBox = max(min(offset, 0));
		return unsignedDst + dstInsideBox;
	}

	private static Double2 abs(Double2 v) {
		final Double2 result = new Double2();
		result.x = Math.abs(v.x);
		result.y = Math.abs(v.y);
		return result;
	}

	private static Double2 max(Double2 v, double value) {
		final Double2 result = new Double2();
		result.x = Math.max(v.x, value);
		result.y = Math.max(v.y, value);
		return result;
	}

	private static Double2 min(Double2 v, double value) {
		final Double2 result = new Double2();
		result.x = Math.min(v.x, value);
		result.y = Math.min(v.y, value);
		return result;
	}

	private static double max(Double2 v) {
		return Math.max(v.x, v.y);
	}

}
