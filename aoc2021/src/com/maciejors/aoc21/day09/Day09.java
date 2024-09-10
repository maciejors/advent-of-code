package com.maciejors.aoc21.day09;

import com.maciejors.aoc21.shared.CommonFunctions;
import com.maciejors.aoc21.shared.Matrix;

import java.util.*;

public class Day09 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("09", false);
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    private static Matrix<Integer> parseCaveMap(List<String> input) {
        List<List<Integer>> matrixValues = new ArrayList<>();
        for (String line : input) {
            matrixValues.add(
                    Arrays.stream(line.split(""))
                            .map(Integer::parseInt)
                            .toList()
            );
        }
        return new Matrix<>(matrixValues);
    }

    private static List<Point> getNeighbours(Matrix<Integer> caveMap, int row, int col) {
        List<Point> neighbours = new ArrayList<>();
        int[] caveMapShape = caveMap.getShape();
        int[][] possibleDiffs = new int[][]{{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        // find neighbours
        for (int[] diffs : possibleDiffs) {
            int neighbourRow = row + diffs[0];
            int neighbourCol = col + diffs[1];
            if (neighbourRow >= 0 &&
                    neighbourRow <= caveMapShape[0] - 1 &&
                    neighbourCol >= 0 &&
                    neighbourCol <= caveMapShape[1] - 1) {
                neighbours.add(new Point(
                        neighbourRow, neighbourCol,
                        caveMap.get(neighbourRow, neighbourCol)
                ));
            }
        }
        return neighbours;
    }

    private static List<Point> findLowPoints(Matrix<Integer> caveMap) {
        List<Point> lowPoints = new ArrayList<>();
        int[] matrixShape = caveMap.getShape();
        for (int row = 0; row < matrixShape[0]; row++) {
            for (int col = 0; col < matrixShape[1]; col++) {
                int heightHere = caveMap.get(row, col);
                List<Point> neighbours = getNeighbours(caveMap, row, col);
                int smallestNeighbourHeight = neighbours.stream()
                        .map(Point::value)
                        .min(Integer::compareTo)
                        .orElse(heightHere);
                if (heightHere < smallestNeighbourHeight) {
                    lowPoints.add(new Point(row, col, heightHere));
                }
            }
        }
        return lowPoints;
    }

    private static void task1(List<String> input) {
        Matrix<Integer> caveMap = parseCaveMap(input);
        List<Point> lowPoints = findLowPoints(caveMap);
        int riskLevelSum = 0;
        for (Point point : lowPoints) {
            riskLevelSum += point.value() + 1;
        }
        System.out.println(riskLevelSum);
    }

    /**
     * Track a basin upwards & downwards from any point of the basin.
     */
    private static Set<Point> traceBasinFrom(Point startingPoint,
                                             Matrix<Integer> caveMap,
                                             Matrix<Boolean> isPartOfAnyBasinLookup) {
        Set<Point> result = new HashSet<>();
        int currRow = startingPoint.row();
        int currCol = startingPoint.col();
        // if the current point is already a part of some basin, it means this basin
        // has been mapped out, so return an 'empty basin'
        if (isPartOfAnyBasinLookup.get(currRow, currCol)) {
            return result;
        }
        // otherwise trace the basin
        result.add(startingPoint);
        isPartOfAnyBasinLookup.set(currRow, currCol, true);
        List<Point> neighbours = getNeighbours(caveMap, currRow, currCol);
        for (Point neighbour : neighbours) {
            if (neighbour.value() < 9) {
                Set<Point> flowFromNeighbour = traceBasinFrom(neighbour, caveMap, isPartOfAnyBasinLookup);
                result.addAll(flowFromNeighbour);
            }
        }
        return result;
    }

    private static void task2(List<String> input) {
        Matrix<Integer> caveMap = parseCaveMap(input);
        int[] matrixShape = caveMap.getShape();
        List<Point> lowPoints = findLowPoints(caveMap);
        List<Set<Point>> basins = new ArrayList<>();
        Matrix<Boolean> isPartOfAnyBasinLookup = Matrix.full(matrixShape[0], matrixShape[1], false);
        // find the basins - start from the low points
        for (Point lowPoint: lowPoints) {
            Set<Point> basin = traceBasinFrom(lowPoint, caveMap, isPartOfAnyBasinLookup);
            // means that this low point is a part of some already traced basin
            if (!basin.isEmpty()) {
                basins.add(basin);
            }
        }
        // find the 3 largest basins sizes
        List<Integer> basinsSizes = basins.stream()
                .map(Set::size)
                .sorted(Comparator.reverseOrder())
                .toList();
        int result = basinsSizes.stream()
                .limit(3L)
                .reduce((currValue, newValue) -> currValue * newValue)
                .orElse(1);
        System.out.println(result);
    }
}

record Point(int row, int col, int value) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;

        return row == point.row && col == point.col && value == point.value;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        result = 31 * result + value;
        return result;
    }
}
