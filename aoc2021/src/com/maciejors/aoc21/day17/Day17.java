package com.maciejors.aoc21.day17;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("17", false);
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    private static TargetArea parseTargetArea(List<String> input) {
        Pattern pattern = Pattern.compile(".*x=(\\d+)\\.\\.(\\d+), y=(-?\\d+)\\.\\.(-?\\d+)");
        Matcher matcher = pattern.matcher(input.get(0));
        if (!matcher.find()) {
            throw new RuntimeException("Failed to parse puzzle input");
        }
        int xMin = Integer.parseInt(matcher.group(1));
        int xMax = Integer.parseInt(matcher.group(2));
        int yMin = Integer.parseInt(matcher.group(3));
        int yMax = Integer.parseInt(matcher.group(4));
        return new TargetArea(xMin, xMax, yMin, yMax);
    }

    private static void task1(List<String> input) {
        TargetArea targetArea = parseTargetArea(input);
        ThrowCalculator opt = new ThrowCalculator(targetArea);
        // x velocity cannot be higher than a distance to the target
        int vUpperBound = targetArea.xMax();
        int uUpperBound = 1000;
        int vBest = 0;
        int uBest = 0;
        int bestHeight = 0;
        for (int v = 1; v <= vUpperBound; v++) {
            for (int u = 1; u <= uUpperBound; u++) {
                // check constraints
                if (!opt.checkConstraints(v, u)) {
                    continue;
                }
                int height = opt.maxHeight(u);
                if (height > bestHeight) {
                    vBest = v;
                    uBest = u;
                    bestHeight = height;
                }
            }
        }
        System.out.printf("Best starting velocity: %d, %d (max height: %d)\n", vBest, uBest, bestHeight);
    }

    private static void task2(List<String> input) {
        TargetArea targetArea = parseTargetArea(input);
        ThrowCalculator opt = new ThrowCalculator(targetArea);
        // x velocity cannot be higher than a distance to the target
        int vUpperBound = targetArea.xMax();
        int uSymmetricBound = 1000;
        int validVelocitiesCount = 0;
        for (int v = 1; v <= vUpperBound; v++) {
            for (int u = -uSymmetricBound; u < uSymmetricBound; u++) {
                if (opt.checkConstraints(v, u)) {
                    validVelocitiesCount++;
                }
            }
        }
        System.out.println(validVelocitiesCount);

    }
}
