package com.maciejors.aoc21.day15;

import com.maciejors.aoc21.shared.CommonFunctions;
import com.maciejors.aoc21.shared.Matrix;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class Day15 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("15", false);
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    private static List<List<Integer>> parseValues(List<String> input) {
        List<List<Integer>> values = new ArrayList<>();
        for (String line : input) {
            values.add(
                    Arrays.stream(line.split(""))
                            .map(Integer::parseInt)
                            .toList()
            );
        }
        return values;
    }

    private static Matrix<Position> getPositions(Matrix<Integer> values, boolean makeDag) {
        int height = values.getShape()[0];
        int width = values.getShape()[1];
        // create a map of positions - for now with no neighbours
        Matrix<Position> positions = Matrix.full(height, width, null);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Position pos = new Position(col, row, values.get(row, col), new ArrayList<>());
                positions.set(row, col, pos);
            }
        }
        // assign neighbours
        int[][] diffsAround;
        if (makeDag) {
            diffsAround = new int[][]{{0, 1}, {1, 0}};
        } else {
            diffsAround = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
        }
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Position currPos = positions.get(row, col);
                for (int[] diff : diffsAround) {
                    int neighbourRow = row + diff[0];
                    int neighbourCol = col + diff[1];
                    if (neighbourRow >= 0 && neighbourRow < height &&
                            neighbourCol >= 0 && neighbourCol < width) {
                        Position neighbour = positions.get(neighbourRow, neighbourCol);
                        currPos.neighbours().add(neighbour);
                    }
                }
            }
        }
        return positions;
    }

    private static Matrix<Position> getPositions(Matrix<Integer> values) {
        return getPositions(values, false);
    }

    /**
     * Run Dijkstra on a matrix. The starting position is assumed to be in the
     * upper-left corner, and the finishing position is in the bottom-right corner.
     * <p>
     * Nodes are positions, adjacent
     * positions are connected, distance to any adjacent node is the risk level
     * of this node.
     */
    private static int dijkstra(Matrix<Position> positions) {
        Position startingPosition = positions.get(0, 0);
        Position finishingPosition = positions.get(
                positions.getShape()[0] - 1,
                positions.getShape()[1] - 1
        );
        // initialise data for Dijkstra
        Map<Position, Integer> unvisitedDistances = positions.toFlatList()
                .stream()
                .collect(Collectors.toMap(key -> key, value -> Integer.MAX_VALUE));
        unvisitedDistances.put(startingPosition, 0);
        // run Dijsktra algorithm
        while (true) {
            Map.Entry<Position, Integer> unvisitedWithSmallestDistanceEntry = unvisitedDistances
                    .entrySet()
                    .stream()
                    .min(Comparator.comparingInt(Map.Entry::getValue))
                    .orElse(null);
            // check stop condition:
            // no unvisited nodes left || all unvisited nodes unreachable || we are at the finish
            if (unvisitedWithSmallestDistanceEntry == null
                    || unvisitedWithSmallestDistanceEntry.getValue() == Integer.MAX_VALUE
                    || unvisitedWithSmallestDistanceEntry.getKey() == finishingPosition) {
                break;
            }
            Position currPos = unvisitedWithSmallestDistanceEntry.getKey();
            int currDistance = unvisitedWithSmallestDistanceEntry.getValue();
            // update unvisited neighbours
            for (Position neighbour : currPos.neighbours()) {
                if (!unvisitedDistances.containsKey(neighbour)) {
                    continue;
                }
                int distanceToNeighbour = neighbour.riskLevel();
                unvisitedDistances.compute(neighbour, (k, currNeighbourDistance) ->
                        Math.min(currDistance + distanceToNeighbour, currNeighbourDistance)
                );
            }
            // mark the current node as visited
            unvisitedDistances.remove(currPos);
        }
        // find the shortest distance to the finishing position
        return unvisitedDistances.get(finishingPosition);
    }

    record PositionAndDistance(Position position, int distance) {}

    /**
     * Dijkstra implementation with Priority Queue
     * <a href="https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-using-priority_queue-stl/">
     *     Dijkstra with Priority Queue
     * </a>
     */
    private static int dijkstraFaster(Matrix<Position> positions) {
        Position startingPosition = positions.get(0, 0);
        Position finishingPosition = positions.get(
                positions.getShape()[0] - 1,
                positions.getShape()[1] - 1
        );
        // initialise data for Dijkstra
        Map<Position, Integer> distances = positions.toFlatList()
                .stream()
                .collect(Collectors.toMap(key -> key, value -> Integer.MAX_VALUE));
        distances.put(startingPosition, 0);
        PriorityQueue<PositionAndDistance> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(PositionAndDistance::distance)
        );
        priorityQueue.add(new PositionAndDistance(startingPosition, 0));
        // run Dijsktra algorithm
        while (!priorityQueue.isEmpty()) {
            PositionAndDistance posWithSmallestDistance = priorityQueue.poll();
            Position currPos = posWithSmallestDistance.position();
            int currPosDistance = posWithSmallestDistance.distance();
            for (Position neighbour : currPos.neighbours()) {
                int distanceToNeighbour = neighbour.riskLevel();
                if (distances.get(neighbour) > currPosDistance + distanceToNeighbour) {
                    distances.put(neighbour, currPosDistance + distanceToNeighbour);
                    priorityQueue.add(new PositionAndDistance(neighbour, distances.get(neighbour)));
                }
            }
        }
        // find the shortest distance to the finishing position
        return distances.get(finishingPosition);
    }

    @Deprecated
    private static void deepFirstSearch(Position vertex, Set<Position> unvisitedSet, Deque<Position> stack) {
        unvisitedSet.remove(vertex);
        for (Position neighbour : vertex.neighbours()) {
            if (unvisitedSet.contains(neighbour)) {
                deepFirstSearch(neighbour, unvisitedSet, stack);
            }
        }
        stack.push(vertex);
    }

    /**
     * This ended up not working
     * <br>
     * <a href="https://www.geeksforgeeks.org/shortest-path-for-directed-acyclic-graphs/">Shortest path for DAGs</a>
     * <a href="https://www.geeksforgeeks.org/topological-sorting/">Topological sorting</a>
     * <a href="https://en.wikipedia.org/wiki/Depth-first_search">Deep-first search</a>
     */
    @Deprecated
    private static int shortestPathInDag(Matrix<Position> positions) {
        Position startingPosition = positions.get(0, 0);
        Position finishingPosition = positions.get(
                positions.getShape()[0] - 1,
                positions.getShape()[1] - 1
        );
        // Initialise data for Topological sorting
        Deque<Position> stack = new LinkedBlockingDeque<>();
        Set<Position> unvisited = new HashSet<>(positions.toFlatList());
        // perform topological sorting
        deepFirstSearch(startingPosition, unvisited, stack);
        List<Position> sortedPositions = new LinkedList<>();
        while (!stack.isEmpty()) {
            sortedPositions.add(stack.pop());
        }
        // Finding the shortest path
        Map<Position, Integer> distances = positions.toFlatList()
                .stream()
                .collect(Collectors.toMap(key -> key, value -> Integer.MAX_VALUE));
        distances.put(startingPosition, 0);
        for (Position currPos : sortedPositions) {
            for (Position adjacentPos : currPos.neighbours()) {
                distances.put(
                        adjacentPos,
                        Math.min(distances.get(adjacentPos), distances.get(currPos) + adjacentPos.riskLevel())
                );
            }
        }
        return distances.get(finishingPosition);
    }

    private static void task1(List<String> input) {
        List<List<Integer>> values = parseValues(input);
        Matrix<Integer> valuesMatrix = new Matrix<>(values);
        Matrix<Position> positions = getPositions(valuesMatrix);
        int distanceToEnd = dijkstraFaster(positions);
        System.out.println(distanceToEnd);
    }

    private static void task2(List<String> input) {
        List<List<Integer>> values = parseValues(input);
        int originalHeight = values.size();
        // Task 2 quirk: make values larger
        // 1. extend the width
        for (int rowId = 0; rowId < originalHeight; rowId++) {
            List<Integer> originalRow = values.get(rowId);
            List<Integer> newRow = new ArrayList<>();
            for (int copyNo = 0; copyNo < 5; copyNo++) {
                final int finalCopyNo = copyNo;
                List<Integer> copyVals = originalRow.stream()
                        .map(v -> (v + finalCopyNo - 1) % 9 + 1)
                        .toList();
                newRow.addAll(copyVals);
            }
            values.set(rowId, newRow);
        }
        // 2. extend the heights
        for (int copyNo = 1; copyNo < 5; copyNo++) {
            for (int rowId = 0; rowId < originalHeight; rowId++) {
                List<Integer> row = values.get(rowId);
                final int finalCopyNo = copyNo;
                List<Integer> extraRow = row.stream()
                        .map(v -> (v + finalCopyNo - 1) % 9 + 1)
                        .toList();
                values.add(extraRow);
            }
        }
        // rest similar to before but algorithm optimised for DAGs
        Matrix<Integer> valuesMatrix = new Matrix<>(values);
        Matrix<Position> positions = getPositions(valuesMatrix, false);
        int distanceToEnd = dijkstraFaster(positions);
        System.out.println(distanceToEnd);
    }
}

record Position(int x, int y, int riskLevel, List<Position> neighbours) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;

        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}