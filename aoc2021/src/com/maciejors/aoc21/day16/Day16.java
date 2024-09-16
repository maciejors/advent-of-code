package com.maciejors.aoc21.day16;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.util.List;

public class Day16 {

    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("16");
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    private static int sumVersionsInsidePacket(Packet packet) {
        int sum = packet.version();
        for (Packet containedPacket : packet.containedPackets()) {
            sum += sumVersionsInsidePacket(containedPacket);
        }
        return sum;
    }

    private static void task1(List<String> input) {
        String hexTransmission = input.get(0);
        Packet outermostPacket = Packet.parsePacket(hexTransmission);
        System.out.println(sumVersionsInsidePacket(outermostPacket));
    }

    private static void task2(List<String> input) {
        String hexTransmission = input.get(0);
        Packet outermostPacket = Packet.parsePacket(hexTransmission);
        System.out.println(outermostPacket.getValue());
    }
}