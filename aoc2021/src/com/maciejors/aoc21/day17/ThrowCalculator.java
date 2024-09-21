package com.maciejors.aoc21.day17;

public class ThrowCalculator {
    private final TargetArea targetArea;

    public ThrowCalculator(TargetArea targetArea) {
        this.targetArea = targetArea;
    }

    public int maxHeight(int u) {
        return u * (u + 1) / 2;
    }

    public int xi(int i, int v) {
        int min_i_v = Math.min(i, v);
        return min_i_v * v - (min_i_v * (min_i_v - 1)) / 2;
    }

    public int yi(int i, int u) {
        return i * u - (i * (i - 1)) / 2;
    }

    /**
     * True if the probe eventually lands in the target area, or false otherwise
     */
    public boolean checkConstraints(int v, int u) {
        int x = 0;
        int y = 0;
        int i = 0;
        for (; x <= targetArea.xMax() && y >= targetArea.yMin(); i++) {
            x = xi(i, v);
            y = yi(i, u);
            if (targetArea.contains(x, y)) {
                return true;
            }
        }
        return false;
    }
}
