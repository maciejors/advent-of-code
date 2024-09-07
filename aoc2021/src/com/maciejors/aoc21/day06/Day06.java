package com.maciejors.aoc21.day06;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day06 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("06", false);
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    /**
     * Returns the following map:
     * - key: number of days until a new fish is created
     * - value: number of fish which will create new fish after `key` days
     */
    private static Map<Integer, Long> parseInitialState(List<String> input) {
        // only 1st line contains anything
        List<Integer> numbers = Arrays.stream(input.get(0).split(","))
                .map(Integer::parseInt)
                .toList();
        Map<Integer, Long> result = new HashMap<>();
        for (int number : numbers) {
            long currNumberCount = result.getOrDefault(number, 0L);
            result.put(number, currNumberCount + 1);
        }
        return result;
    }

    private static long simulate(Map<Integer, Long> initState, int numberOfDays) {
        Map<Integer, Long> state = initState;
        // simulate
        for (int i = 0; i < numberOfDays; i++) {
            Map<Integer, Long> newState = new HashMap<>();
            // handle shift (reduction in number of days by 1)
            for (int daysUntilCreation : state.keySet()) {
                if (daysUntilCreation > 0) {
                    newState.put(
                            daysUntilCreation - 1,
                            state.getOrDefault(daysUntilCreation, 0L)
                    );
                }
            }
            // handle newly created
            long createdFishCount = state.getOrDefault(0, 0L);
            newState.put(6, newState.getOrDefault(6, 0L) + createdFishCount);
            newState.put(8, createdFishCount);
            state = newState;
        }
        // count
        long countOfFish = state.values()
                .stream()
                .reduce(Long::sum)
                .orElse(0L);
        return countOfFish;
    }

    private static void task1(List<String> input) {
        Map<Integer, Long> initState = parseInitialState(input);
        long finalFishCount = simulate(initState, 80);
        System.out.println(finalFishCount);
    }

    private static void task2(List<String> input) {
        Map<Integer, Long> initState = parseInitialState(input);
        long finalFishCount = simulate(initState, 256);
        System.out.println(finalFishCount);
    }
}