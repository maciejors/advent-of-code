package com.maciejors.aoc21.day14;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("14", true);
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    private static PolymerData parsePolymerData(List<String> input) {
        String templateStr = input.get(0);
        Map<String, Character> pairInsertionRules = new HashMap<>();
        for (int i = 2; i < input.size(); i++) {
            String[] lineSplit = input.get(i).split(" -> ");
            pairInsertionRules.put(lineSplit[0], lineSplit[1].charAt(0));
        }
        return new PolymerData(templateStr, pairInsertionRules);
    }

    private static int simulatePolymerGrowthSlow(PolymerData polymerData, int steps) {
        // 1. simulate growth
        Map<String, Character> insertionRules = polymerData.pairInsertionRules();
        String currPolymer = polymerData.template();
        for (int step = 1; step <= steps; step++) {
            StringBuilder nextPolymer = new StringBuilder();
            for (int i = 0; i < currPolymer.length() - 1; i++) {
                String pair = currPolymer.substring(i, i + 2);
                nextPolymer.append(pair.charAt(0));
                Character replacement = insertionRules.getOrDefault(pair, null);
                if (replacement != null) {
                    nextPolymer.append(replacement);
                }
            }
            // append the last element
            nextPolymer.append(currPolymer.charAt(currPolymer.length() - 1));
            currPolymer = nextPolymer.toString();
        }

        // 2. calculate score
        Map<Character, Integer> elementsCounts = new HashMap<>();
        for (char character : currPolymer.toCharArray()) {
            elementsCounts.put(character, elementsCounts.getOrDefault(character, 0) + 1);
        }
        int mostCommonCount = elementsCounts.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getValue)
                .orElse(0);
        int leastCommonCount = elementsCounts.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getValue)
                .orElse(0);
        return mostCommonCount - leastCommonCount;
    }

    private static int simulatePolymerGrowthFast(PolymerData polymerData, int steps) {
        return 0;
    }

    private static void task1(List<String> input) {
        PolymerData polymerData = parsePolymerData(input);
        int result = simulatePolymerGrowthSlow(polymerData, 10);
        System.out.println(result);
    }

    private static void task2(List<String> input) {
        PolymerData polymerData = parsePolymerData(input);
        int result = simulatePolymerGrowthFast(polymerData, 40);
        System.out.println(result);
    }
}

record PolymerData(String template, Map<String, Character> pairInsertionRules) {}