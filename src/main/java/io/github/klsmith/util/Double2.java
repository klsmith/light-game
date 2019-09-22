package io.github.klsmith.util;

public class Double2 {

    private final double x;
    private final double y;

    public Double2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Double2 subtract(Double2 other) {
        return new Double2(
                getX() - other.getX(),
                getY() - other.getY());
    }

    public Double2 abs() {
        return new Double2(
                Math.abs(getX()),
                Math.abs(getY()));
    }

    public double max() {
        return Math.max(getX(), getY());
    }

    public Double2 max(double value) {
        return new Double2(
                Math.max(getX(), value),
                Math.max(getY(), value));
    }

    public Double2 min(double value) {
        return new Double2(
                Math.min(getX(), value),
                Math.min(getY(), value));
    }

}
