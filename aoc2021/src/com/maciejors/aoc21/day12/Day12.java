package com.maciejors.aoc21.day12;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.util.*;

public class Day12 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("12", "input.txt");
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    /**
     * Key: cave name
     * Value: list of cave names which are connected to this cave
     */
    private static Map<String, List<String>> parseConnections(List<String> input) {
        Map<String, List<String>> result = new HashMap<>();
        for (String line : input) {
            String[] connectedPair = line.split("-");
            // add to each other entries
            for (int i = 0; i <= 1; i++) {
                List<String> currConnections = result.getOrDefault(connectedPair[i], new ArrayList<>());
                currConnections.add(connectedPair[1 - i]);
                result.put(connectedPair[i], currConnections);
            }
        }
        return result;
    }

    private static boolean isLowerCase(String s) {
        return s.toLowerCase().equals(s);
    }

    private static List<List<String>> getPathsFrom(
            String startingCave,
            Map<String, List<String>> connections,
            Set<String> alreadyVisited
    ) {
        // copy the set
        alreadyVisited = new HashSet<>(alreadyVisited);
        alreadyVisited.add(startingCave);

        List<List<String>> paths = new ArrayList<>();
        // handle the end of path
        if (startingCave.equals("end")) {
            List<String> finishingPath = new LinkedList<>();
            finishingPath.add(startingCave);
            paths.add(finishingPath);
            return paths;
        }

        List<String> connectedCaves = connections.get(startingCave);
        for (String cave : connectedCaves) {
            // skip small caves which have already been visited
            if (isLowerCase(cave) && alreadyVisited.contains(cave)) {
                continue;
            }
            List<List<String>> foundPaths = getPathsFrom(cave, connections, alreadyVisited);
            // prepend the current cave into found paths
            for (List<String> foundPath : foundPaths) {
                foundPath.add(0, startingCave);
            }
            paths.addAll(foundPaths);
        }
        return paths;
    }

    private static void task1(List<String> input) {
        Map<String, List<String>> connections = parseConnections(input);
        List<List<String>> paths = getPathsFrom("start", connections, new HashSet<>());
        System.out.println(paths.size());
    }

    private static List<List<String>> getPathsFrom2(
            String startingCave,
            Map<String, List<String>> connections,
            Set<String> alreadyVisited,
            boolean isDoubleSmallCaveVisitUsed
    ) {
        List<List<String>> paths = new ArrayList<>();
        // handle visiting small cave more than once
        if (!startingCave.equals("start") && isLowerCase(startingCave) && alreadyVisited.contains(startingCave)) {
            // handle wildcard
            if (isDoubleSmallCaveVisitUsed) {
                return paths;
            } else {
                isDoubleSmallCaveVisitUsed = true;
            }
        }

        // handle the end of path
        if (startingCave.equals("end")) {
            List<String> finishingPath = new LinkedList<>();
            finishingPath.add(startingCave);
            paths.add(finishingPath);
            return paths;
        }
        // copy the set
        alreadyVisited = new HashSet<>(alreadyVisited);
        alreadyVisited.add(startingCave);

        List<String> connectedCaves = connections.get(startingCave);
        for (String cave : connectedCaves) {
            // do not visit start twice
            if (cave.equals("start")) {
                continue;
            }
            List<List<String>> foundPaths = getPathsFrom2(
                    cave,
                    connections,
                    alreadyVisited,
                    isDoubleSmallCaveVisitUsed
            );
            // prepend the current cave into found paths
            for (List<String> foundPath : foundPaths) {
                foundPath.add(0, startingCave);
            }
            paths.addAll(foundPaths);
        }
        return paths;
    }

    private static void task2(List<String> input) {
        Map<String, List<String>> connections = parseConnections(input);
        List<List<String>> paths = getPathsFrom2(
                "start",
                connections,
                new HashSet<>(),
                false
        );
        System.out.println(paths.size());
    }
}