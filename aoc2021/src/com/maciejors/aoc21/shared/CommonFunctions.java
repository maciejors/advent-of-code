package com.maciejors.aoc21.shared;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CommonFunctions {

    public static int binToDec(String bin) {
        int value = 0;
        for (int i = 0; i < bin.length(); i++) {
            if (Integer.parseInt(String.valueOf(bin.charAt(i))) == 1) {
                value += (int) Math.pow(2, bin.length() - i - 1);
            }
        }
        return value;
    }

    /**
     * Read input for a specified day
     * @param dayId A day identifier (e.g. "02", "09", "15", "21")
     * @param filename Which file to read (e.g. "example-2.txt")
     * @return A list of lines from the input file
     */
    public static List<String> readInput(String dayId, String filename) {
        String path = "src/com/maciejors/aoc21/day%s/%s".formatted(dayId, filename);
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Read input for a specified day
     * @param dayId A day identifier (e.g. "02", "09", "15", "21")
     * @param example If true, it will read "example-type4.txt". Otherwise, "input.txt"
     * @return A list of lines from the input file
     */
    public static List<String> readInput(String dayId, boolean example) {
        if (example) {
            return readInput(dayId, "example-type4.txt");
        } else {
            return readInput(dayId, "input.txt");
        }
    }

    /**
     * Read input for a specified day
     */
    public static List<String> readInput(String dayId) {
        return readInput(dayId, false);
    }
}
