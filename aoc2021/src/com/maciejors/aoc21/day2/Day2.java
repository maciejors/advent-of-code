package com.maciejors.aoc21.day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day2 {
    public static void main(String[] args) {
        List<String> values = new ArrayList<>(2000);
        try (BufferedReader reader = new BufferedReader(new FileReader("src/com/maciejors/aoc21/day2/input.txt"))) {
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
        int position = 0;
        int depth = 0;
        for (String value : values) {
            String command  = value.split(" ")[0];
            int n = Integer.parseInt(value.split(" ")[1]);
            switch (command) {
                case "forward":
                    position += n;
                    break;
                case "up":
                    depth -= n;
                    break;
                case "down":
                    depth += n;
                    break;
            }
        }
        return position * depth;
    }

    private static int task2(List<String> values) {
        int aim = 0;
        int position = 0;
        int depth = 0;
        for (String value : values) {
            String command  = value.split(" ")[0];
            int n = Integer.parseInt(value.split(" ")[1]);
            switch (command) {
                case "forward":
                    position += n;
                    depth += aim * n;
                    break;
                case "up":
                    aim -= n;
                    break;
                case "down":
                    aim += n;
                    break;
            }
        }
        return position * depth;
    }
}
