package com.maciejors.aoc21.day05;

import java.util.ArrayList;
import java.util.List;

public record Line(Point start, Point end) {

    public boolean isHorizontal() {
        return start.y() == end.y();
    }

    public boolean isVertical() {
        return start.x() == end.x();
    }

    public double length() {
        return start.distanceFrom(end);
    }

    public List<Point> points() {
        List<Point> result = new ArrayList<>();

        double diffX = end.x() - start.x();
        double diffY = end.y() - start.y();
        int stepX = (int) Math.signum(diffX);
        int stepY = (int) Math.signum(diffY);

        result.add(start);

        int x = start.x();
        int y = start.y();
        int steps = (int) Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int i = 0; i < steps; i++) {
            x = x + stepX;
            y = y + stepY;
            result.add(new Point(x, y));
        }
        return result;
    }
}
