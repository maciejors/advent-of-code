package com.maciejors.aoc21.day07;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.util.Arrays;
import java.util.List;

public class Day07 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("07", false);
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    private static List<Integer> parsePositions(List<String> input) {
        return Arrays.stream(input.get(0).split(","))
                .map(Integer::parseInt)
                .toList();
    }

    private static int calculateFuelUsed1(List<Integer> positions, int targetPosition) {
        int fuelUsed = 0;
        for (int pos : positions) {
            fuelUsed += Math.abs(pos - targetPosition);
        }
        return fuelUsed;
    }

    private static int calculateFuelUsed2(List<Integer> positions, int targetPosition) {
        int fuelUsed = 0;
        for (int pos : positions) {
            int diff = Math.abs(pos - targetPosition);
            fuelUsed += diff * (diff + 1) / 2;
        }
        return fuelUsed;
    }

    private static void task1(List<String> input) {
        List<Integer> positions = parsePositions(input);
        // AIM: minimise MAE.
        // A median minimises MAE
        List<Integer> sortedPositions = positions.stream()
                .sorted()
                .toList();
        int numberOfCrabs = sortedPositions.size();
        int median;
        if (numberOfCrabs % 2 == 0) {
            // mean of central elements
            int medianLeft = sortedPositions.get(numberOfCrabs / 2 - 1);
            int medianRight = sortedPositions.get(numberOfCrabs / 2);
            median = (int) Math.round(((double) medianLeft + medianRight) / 2);
        } else {
            median = sortedPositions.get(numberOfCrabs / 2);
        }
        // calculate total fuel used
        int fuelUsed = calculateFuelUsed1(positions, median);
        System.out.println(fuelUsed);
    }

    private static void task2(List<String> input) {
        List<Integer> positions = parsePositions(input);
        // it can be derived that:
        // AIM: min{MSE + MAE}
        // gradient descent
        double lr = 0.01;
        // starting point: mean
        int nOfPositions = positions.size();
        double targetPositionExact = positions.stream()
                .mapToDouble(p -> p)
                .average()
                .orElse(0.0);
        int nEpochs = 1000;
        for (int i = 0; i < nEpochs; i++) {
            double currPrediction = targetPositionExact;
            // gradient of loss
            // mean(x_i - y)
            double meanOfDiff = positions.stream()
                    .mapToDouble(p -> p - currPrediction)
                    .average()
                    .orElse(0.0);
            double gradMSE = -2.0 * meanOfDiff;
            // mean(sign(x_i - y))
            double meanOfSignDiff = positions.stream()
                    .mapToDouble(p -> p - currPrediction)
                    .map(Math::signum)
                    .average()
                    .orElse(0.0);
            double gradMAE = -1.0 * meanOfSignDiff;
            // add the two gradients
            double grad = gradMSE + gradMAE;
            // gradient descent
            targetPositionExact -= lr * grad;
        }
        int positionRounded = (int) Math.round(targetPositionExact);
        int fuelUsed = calculateFuelUsed2(positions, positionRounded);
        System.out.printf("Position exact: %f\n", targetPositionExact);
        System.out.printf("Position rounded: %d\n", positionRounded);
        System.out.printf("Fuel used: %d\n", fuelUsed);
    }
}