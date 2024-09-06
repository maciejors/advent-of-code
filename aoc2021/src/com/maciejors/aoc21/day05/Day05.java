package com.maciejors.aoc21.day05;

import com.maciejors.aoc21.shared.CommonFunctions;
import com.maciejors.aoc21.shared.Matrix;

import java.util.ArrayList;
import java.util.List;

public class Day05 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("05", false);
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    private static List<Line> parseLines(List<String> input) {
        List<Line> result = new ArrayList<>(input.size());
        for (String rawLine : input) {
            String[] rawPoints = rawLine.split(" -> ");
            Point lineStart = Point.parsePoint(rawPoints[0]);
            Point lineEnd = Point.parsePoint(rawPoints[1]);
            result.add(new Line(lineStart, lineEnd));
        }
        return result;
    }

    private static int countOverlaps(List<Line> lines) {
        int diagramSize = 1000;
        Matrix<Integer> diagram = Matrix.full(diagramSize, diagramSize, 0);
        int overlapsCount = 0;
        for (Line line : lines) {
            for (Point point : line.points()) {
                int x = point.x();
                int y = point.y();
                int diagramValue = diagram.get(y, x);
                diagram.set(y, x, diagramValue + 1);
                if (diagramValue + 1 == 2) {
                    overlapsCount++;
                }
            }
        }
        return overlapsCount;
    }

    private static void task1(List<String> input) {
        List<Line> lines = parseLines(input);
        List<Line> horizontalOrVerticalLines = lines.stream()
                .filter(line -> line.isVertical() || line.isHorizontal())
                .toList();
        int overlapsCount = countOverlaps(horizontalOrVerticalLines);
        System.out.println(overlapsCount);
    }

    private static void task2(List<String> input) {
        List<Line> lines = parseLines(input);
        int overlapsCount = countOverlaps(lines);
        System.out.println(overlapsCount);
    }
}