package com.maciejors.aoc21.day17;

public record TargetArea(int xMin, int xMax, int yMin, int yMax) {
    public boolean contains(int x, int y) {
        return x >= xMin && x <= xMax && y >= yMin && y <= yMax;
    }
}
