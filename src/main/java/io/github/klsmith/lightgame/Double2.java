package io.github.klsmith.lightgame;

public class Double2 {

	double x;
	double y;

	Double2 subtract(Double2 other) {
		final Double2 result = new Double2();
		result.x = x - other.x;
		result.y = y - other.y;
		return result;
	}

}
