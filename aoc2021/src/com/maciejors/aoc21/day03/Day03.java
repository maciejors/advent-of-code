package com.maciejors.aoc21.day03;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day03 {
    public static void main(String[] args) {
        List<String> values = new ArrayList<>(2000);
        try (BufferedReader reader = new BufferedReader(new FileReader("src/com/maciejors/aoc21/day3/input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                values.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(task1(values));
        System.out.println(task2(values));
    }

    private static int task1(List<String> values) {
        StringBuilder gammaRate = new StringBuilder();
        StringBuilder epsilonRate = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int ones = 0;
            int zeros = 0;
            int finalI = i;
            for (char value : values.stream()
                    .map(v -> v.charAt(finalI))
                    .collect(Collectors.toList())) {
                switch (value) {
                    case '0':
                        zeros++;
                        break;
                    case '1':
                        ones++;
                        break;
                }
            }
            if (ones > zeros) {
                gammaRate.append('1');
                epsilonRate.append('0');
            } else {
                gammaRate.append('0');
                epsilonRate.append('1');
            }
        }
        int gammaRateValue = CommonFunctions.binToDec(gammaRate.toString());
        int epsilonRateValue = CommonFunctions.binToDec(epsilonRate.toString());
        System.out.println(gammaRateValue);
        System.out.println(epsilonRateValue);
        return gammaRateValue * epsilonRateValue;
    }

    private static int task2(List<String> values) {
        int oxygen = 0;
        List<String> valuesCopy1 = new ArrayList<>(values);
        for (int i = 0; i < 12; i++) {
            int finalI = i;
            char toFilter;
            int ones = 0;
            int zeros = 0;
            for (char value : valuesCopy1.stream()
                    .map(v -> v.charAt(finalI))
                    .collect(Collectors.toList())) {
                switch (value) {
                    case '0':
                        zeros++;
                        break;
                    case '1':
                        ones++;
                        break;
                }
            }
            if (zeros <= ones) {
                toFilter = '1';
            } else {
                toFilter = '0';
            }
            valuesCopy1 = valuesCopy1.stream()
                    .filter(bin -> bin.charAt(finalI) == toFilter)
                    .collect(Collectors.toList());
            if (valuesCopy1.size() == 1) {
                oxygen = CommonFunctions.binToDec(valuesCopy1.get(0));
            }
        }
        int co2 = 0;
        List<String> valuesCopy2 = new ArrayList<>(values);
        for (int i = 0; i < 12; i++) {
            int finalI = i;
            char toFilter;
            int ones = 0;
            int zeros = 0;
            for (char value : valuesCopy2.stream()
                    .map(v -> v.charAt(finalI))
                    .collect(Collectors.toList())) {
                switch (value) {
                    case '0':
                        zeros++;
                        break;
                    case '1':
                        ones++;
                        break;
                }
            }
            if (zeros > ones) {
                toFilter = '1';
            } else {
                toFilter = '0';
            }
            valuesCopy2 = valuesCopy2.stream()
                    .filter(bin -> bin.charAt(finalI) == toFilter)
                    .collect(Collectors.toList());
            if (valuesCopy2.size() == 1) {
                co2 = CommonFunctions.binToDec(valuesCopy2.get(0));
            }
        }
        return oxygen * co2;
    }
}
