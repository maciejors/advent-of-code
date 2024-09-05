package com.maciejors.aoc21;

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
     * @param example Read example data
     * @return A list of lines from the input file
     */
    public static List<String> readInput(String dayId, boolean example) {
        String path = "src/com/maciejors/aoc21/day%s/".formatted(dayId);
        if (example) {
            path += "example.txt";
        } else {
            path += "input.txt";
        }
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
}
