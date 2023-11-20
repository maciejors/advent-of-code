package com.maciejors.aoc21.day1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day1 {
    public static void main(String[] args) throws IOException, InterruptedException {
        List<Integer> values = new ArrayList<>(2000);
        try (BufferedReader reader = new BufferedReader(new FileReader("src/com/maciejors/aoc21/day1/input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                values.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(task1(values));
        System.out.println(task2(values));
    }

    private static int task2(List<Integer> values) {
        AtomicInteger prev2 = new AtomicInteger(values.get(0));
        AtomicInteger prev = new AtomicInteger(values.get(1));
        List<Integer> sums = values.stream()
                .skip(2)
                .map(value -> {
                    int sum = value + prev.get() + prev2.get();
                    prev2.set(prev.get());
                    prev.set(value);
                    return sum;
                })
                .collect(Collectors.toList());
        return task1(sums);
    }

    private static int task1(List<Integer> values) {
        int prev = Integer.MAX_VALUE;
        int result = 0;
        for (Integer value : values) {
            if (value > prev) {
                result++;
            }
            prev = value;
        }
        return result;
    }
}
