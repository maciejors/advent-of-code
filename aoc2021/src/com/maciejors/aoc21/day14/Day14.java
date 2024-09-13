package com.maciejors.aoc21.day14;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Day14 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("14", false);
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

    private static BigInteger simulatePolymerGrowthFast(PolymerData polymerData, int steps) {
        String startingPolymer = polymerData.template();
        Map<String, Character> insertionRules = polymerData.pairInsertionRules();
        // track counts
        Map<Character, BigInteger> elementsCounts = new HashMap<>();
        for (char character : startingPolymer.toCharArray()) {
            elementsCounts.put(
                    character,
                    elementsCounts.getOrDefault(character, BigInteger.ZERO).add(BigInteger.ONE)
            );
        }
        // track counts of pairs that are included in insertion rules
        Map<String, BigInteger> importantPairsCounts = insertionRules.keySet()
                .stream()
                .collect(Collectors.toMap(key -> key, value -> BigInteger.ZERO));
        for (int i = 0; i < startingPolymer.length() - 1; i++) {
            String pair = startingPolymer.substring(i, i + 2);
            if (importantPairsCounts.containsKey(pair)) {
                importantPairsCounts.put(pair, importantPairsCounts.get(pair).add(BigInteger.ONE));
            }
        }
        // perform the simulation
        for (int i = 0; i < steps; i++) {
            Map<String, BigInteger> pairsDiffs = new HashMap<>();
            for (Map.Entry<String, BigInteger> entry : importantPairsCounts.entrySet()) {
                String pair = entry.getKey();
                BigInteger pairCount = entry.getValue();
                if (BigInteger.ZERO.equals(pairCount)) {
                    continue;
                }
                char insertedElementChar = insertionRules.get(pair);
                String insertedElementStr = String.valueOf(insertedElementChar);
                // inserting in between adds two new pairs and removes this pair
                pairsDiffs.put(pair, pairsDiffs.getOrDefault(pair, BigInteger.ZERO).subtract(pairCount));
                // but adds two new pairs
                String leftPair = pair.charAt(0) + insertedElementStr;
                String rightPair = insertedElementStr + pair.charAt(1);
                pairsDiffs.put(leftPair, pairsDiffs.getOrDefault(leftPair, BigInteger.ZERO).add(pairCount));
                pairsDiffs.put(rightPair, pairsDiffs.getOrDefault(rightPair, BigInteger.ZERO).add(pairCount));
                // also update element counts with the added element
                elementsCounts.put(
                        insertedElementChar,
                        elementsCounts.getOrDefault(insertedElementChar, BigInteger.ZERO).add(pairCount)
                );
            }
            // update counts with diffs
            importantPairsCounts.replaceAll((p, v) ->
                    importantPairsCounts.get(p).add(pairsDiffs.getOrDefault(p, BigInteger.ZERO))
            );
        }
        // calculate final score
        BigInteger mostCommonCount = elementsCounts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(BigInteger.ZERO);
        BigInteger leastCommonCount = elementsCounts.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(BigInteger.ZERO);
        return mostCommonCount.subtract(leastCommonCount);
    }

    private static void task1(List<String> input) {
        PolymerData polymerData = parsePolymerData(input);
        BigInteger result = simulatePolymerGrowthFast(polymerData, 10);
        System.out.println(result);
    }

    private static void task2(List<String> input) {
        PolymerData polymerData = parsePolymerData(input);
        BigInteger result = simulatePolymerGrowthFast(polymerData, 40);
        System.out.println(result);
    }
}

record PolymerData(String template, Map<String, Character> pairInsertionRules) {}