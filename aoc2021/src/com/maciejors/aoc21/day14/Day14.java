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
        PolymerElement firstElement = PolymerElement.parsePolymer(templateStr);
        Map<String, Character> pairInsertionRules = new HashMap<>();
        for (int i = 2; i < input.size(); i++) {
            String[] lineSplit = input.get(i).split(" -> ");
            pairInsertionRules.put(lineSplit[0], lineSplit[1].charAt(0));
        }
        return new PolymerData(firstElement, pairInsertionRules);
    }

    private static PolymerElement simulatePolymerGrowth(PolymerData polymerData, int steps) {
        Map<String, Character> insertionRules = polymerData.pairInsertionRules();
        PolymerElement firstElement = polymerData.templateFirstElement();
        for (int i = 0; i < steps; i++) {
            System.out.printf("Now calculating step %d\n", i + 1);
            PolymerElement currElement = firstElement;
            while (currElement.hasNext()) {
                String currPair = currElement.getPair();
                Character replacement = insertionRules.getOrDefault(currPair, null);
                if (replacement != null) {
                    currElement.insertNext(replacement);
                    currElement = currElement.moveToNext();
                }
                currElement = currElement.moveToNext();
            }
        }
        return firstElement;
    }

    /**
     * Quantity of the most common element minus q-ty of the least common element
     */
    private static int calculateScore(PolymerElement firstElement) {
        Map<Character, Integer> elementsCounts = new HashMap<>();
        PolymerElement currElement = firstElement;
        while (currElement != null) {
            char currChar = currElement.getValue();
            elementsCounts.put(currChar, elementsCounts.getOrDefault(currChar, 0) + 1);
            currElement = currElement.moveToNext();
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

    private static void task1(List<String> input) {
        PolymerData polymerData = parsePolymerData(input);
        PolymerElement firstElement = simulatePolymerGrowth(polymerData, 10);
        System.out.println(calculateScore(firstElement));
    }

    private static void task2(List<String> input) {
        PolymerData polymerData = parsePolymerData(input);
        PolymerElement firstElement = simulatePolymerGrowth(polymerData, 40);
        System.out.println(calculateScore(firstElement));
    }
}

record PolymerData(PolymerElement templateFirstElement, Map<String, Character> pairInsertionRules) {}