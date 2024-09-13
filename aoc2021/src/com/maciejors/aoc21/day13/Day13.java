package com.maciejors.aoc21.day13;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {
    public static void main(String[] args) {
        List<String> inputLines = CommonFunctions.readInput("13", false);
        System.out.println("Task 1:");
        task1(inputLines);
        System.out.println();
        System.out.println("Task 2:");
        task2(inputLines);
    }

    private static InputData parseInputData(List<String> inputLines) {
        // first bit of input: points
        int i = 0;
        String line = inputLines.get(i);
        Set<Point> points = new HashSet<>();
        while (!line.isEmpty()) {
            List<Integer> coordinates = Arrays.stream(line.split(","))
                    .map(Integer::parseInt)
                    .toList();
            points.add(new Point(coordinates.get(0), coordinates.get(1)));
            i++;
            line = inputLines.get(i);
        }
        // second bit of input: folds
        List<Fold> folds = new ArrayList<>();
        for (int j = i + 1; j < inputLines.size(); j++) {
            line = inputLines.get(j);
            Pattern pattern = Pattern.compile("([xy])=(\\d+)");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                Axis axis = Axis.parseAxis(matcher.group(1));
                int value = Integer.parseInt(matcher.group(2));
                folds.add(new Fold(axis, value));
            }
        }
        return new InputData(points, folds);
    }

    private static void drawPoints(Set<Point> points) {
        int maxX = points.stream().mapToInt(Point::x).max().orElse(0);
        int maxY = points.stream().mapToInt(Point::y).max().orElse(0);
        StringBuilder output = new StringBuilder();
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                if (points.contains(new Point(x, y))) {
                    output.append('#');
                } else {
                    output.append('.');
                }
            }
            output.append('\n');
        }
        System.out.print(output);
    }

    private static int foldCoord(int coord, int axisValue) {
        if (coord < axisValue) {
            return coord;
        } else {
            return 2 * axisValue - coord;
        }
    }

    private static Set<Point> performFold(Fold fold, Set<Point> points) {
        Set<Point> pointsAfterFold = new HashSet<>();
        for (Point point : points) {
            int newX;
            int newY;
            switch (fold.axis()) {
                case X -> {
                    newX = foldCoord(point.x(), fold.value());
                    newY = point.y();
                }
                case Y -> {
                    newX = point.x();
                    newY = foldCoord(point.y(), fold.value());
                }
                default -> {
                    continue;
                }
            }
            pointsAfterFold.add(new Point(newX, newY));
        }
        return pointsAfterFold;
    }

    private static void task1(List<String> inputLines) {
        InputData inputData = parseInputData(inputLines);
        Set<Point> currPoints = inputData.points();
        Fold firstFold = inputData.folds().get(0);
        System.out.println(performFold(firstFold, currPoints).size());
    }

    private static void task2(List<String> inputLines) {
        InputData inputData = parseInputData(inputLines);
        Set<Point> currPoints = inputData.points();
        for (Fold fold : inputData.folds()) {
            currPoints = performFold(fold, currPoints);
        }
        drawPoints(currPoints);
    }
}

record Point(int x, int y) {
    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
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

enum Axis {
    X, Y;

    public static Axis parseAxis(String s) {
        if (s.equals("x")) {
            return X;
        }
        if (s.equals("y")) {
            return Y;
        }
        return null;
    }

    @Override
    public String toString() {
        return switch (this) {
            case X -> "x";
            case Y -> "y";
        };
    }
}

record Fold(Axis axis, int value) {
    @Override
    public String toString() {
        return String.format("fold along %s=%d", axis.toString(), value);
    }
}

record InputData(Set<Point> points, List<Fold> folds) {
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Point point : points) {
            stringBuilder.append(point.toString()).append('\n');
        }
        stringBuilder.append('\n');
        for (Fold fold : folds) {
            stringBuilder.append(fold.toString()).append('\n');
        }
        return stringBuilder.toString();
    }
}
