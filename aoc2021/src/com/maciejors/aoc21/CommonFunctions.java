package com.maciejors.aoc21;

public final class CommonFunctions {

    public static int binToDec(String bin) {
        int value = 0;
        for (int i = 0; i < bin.length(); i++) {
            if (Integer.parseInt(String.valueOf(bin.charAt(i))) == 1) {
                value += Math.pow(2, bin.length() - i - 1);
            }

        }
        return value;
    }
}
