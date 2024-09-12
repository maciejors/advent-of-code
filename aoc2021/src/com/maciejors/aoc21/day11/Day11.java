package com.maciejors.aoc21.day11;

import com.maciejors.aoc21.shared.CommonFunctions;
import com.maciejors.aoc21.shared.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day11 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("11", false);
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    private static Matrix<Integer> parseEnergyLevels(List<String> input) {
        List<List<Integer>> values = new ArrayList<>();
        for (String line : input) {
            values.add(
                    Arrays.stream(line.split(""))
                            .map(Integer::parseInt)
                            .toList()
            );
        }
        return new Matrix<>(values);
    }

    private static void handleFlash(Matrix<Integer> energyLevels, int rowIdx, int colIdx) {
        // The value of -1 will mean that the octopus has flashed in this step
        energyLevels.set(rowIdx, colIdx, -1);
        // increment the neighbours
        int[][] diffsAround = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };
        for (int[] neighbourDiff : diffsAround) {
            int neighbourRow = rowIdx + neighbourDiff[0];
            int neighbourCol = colIdx + neighbourDiff[1];
            // invalid indexes
            if (neighbourRow < 0 || neighbourRow >= 10 ||
                    neighbourCol < 0 || neighbourCol >= 10) {
                continue;
            }
            int neighbourValue = energyLevels.get(neighbourRow, neighbourCol);
            // already flashed - skip
            if (neighbourValue == -1) {
                continue;
            }
            // increment
            energyLevels.set(neighbourRow, neighbourCol, neighbourValue + 1);
            // if this causes the neighbour to flash then handle it
            if (neighbourValue + 1 > 9) {
                handleFlash(energyLevels, neighbourRow, neighbourCol);
            }


        }
    }

    /**
     * Returns the number of flashes during this step
     */
    private static int handleStep(Matrix<Integer> energyLevels) {
        // The value of 11 will mean that the octopus has flashed in the step
        // 1. increment values for all octopuses.
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                energyLevels.set(row, col, energyLevels.get(row, col) + 1);
            }
        }
        // 2. handle flashes
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (energyLevels.get(row, col) > 9) {
                    handleFlash(energyLevels, row, col);
                }
            }
        }
        // 3. reset flashed to 0
        int flashes = 0;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (energyLevels.get(row, col) == -1) {
                    flashes++;
                    energyLevels.set(row, col, 0);
                }
            }
        }
        return flashes;
    }

    private static void task1(List<String> input) {
        Matrix<Integer> energyLevels = parseEnergyLevels(input);
        int totalFlashes = 0;
        for (int step = 1; step <= 100; step++) {
            totalFlashes += handleStep(energyLevels);
        }
        System.out.println(totalFlashes);
    }

    private static void task2(List<String> input) {
        Matrix<Integer> energyLevels = parseEnergyLevels(input);
        int synchronisedStep = 0;
        for (int step = 1; synchronisedStep == 0; step++) {
            int flashesStep = handleStep(energyLevels);
            if (flashesStep == 100) {
                synchronisedStep = step;
            }
        }
        System.out.println(synchronisedStep);
    }
}