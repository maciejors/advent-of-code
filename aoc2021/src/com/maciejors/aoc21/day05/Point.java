package com.maciejors.aoc21.day05;

public record Point(int x, int y) {

    /**
     * Parse from a string like this: "12,45"
     */
    public static Point parsePoint(String s) {
        String[] rawXY = s.split(",");
        int x = Integer.parseInt(rawXY[0]);
        int y = Integer.parseInt(rawXY[1]);
        return new Point(x, y);
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public double distanceFrom(Point other) {
        return Point.distance(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;

        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
