package com.maciejors.aoc21.day16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

/**
 * @param version Packet version
 * @param typeId Packet type
 * @param literalValue Literal value for packet type 4, otherwise -1
 * @param containedPackets Sub-packets, an empty list for packet type 4.
 */
public record Packet(int version, int typeId, long literalValue, List<Packet> containedPackets) {
    private static final List<Character> hexChars = List.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    );
    private static final List<String> hexCharsInBinary = new ArrayList<>(16);

    static {
        for (int bit1 = 0; bit1 < 2; bit1++) {
            for (int bit2 = 0; bit2 < 2; bit2++) {
                for (int bit3 = 0; bit3 < 2; bit3++) {
                    for (int bit4 = 0; bit4 < 2; bit4++) {
                        hexCharsInBinary.add(String.format("%d%d%d%d", bit1, bit2, bit3, bit4));
                    }
                }
            }
        }
    }

    private static String convertHexPacketToBinary(String hexTransmission) {
        StringBuilder binPacket = new StringBuilder();
        for (char hexChar : hexTransmission.toCharArray()) {
            int decValue = hexChars.indexOf(hexChar);
            String binValue = hexCharsInBinary.get(decValue);
            binPacket.append(binValue);
        }
        return binPacket.toString();
    }

    public static long binToDec(String bin) {
        long value = 0;
        for (int i = 0; i < bin.length(); i++) {
            if (bin.charAt(i) == '1') {
                value += (long) Math.pow(2, bin.length() - i - 1);
            }
        }
        return value;
    }

    /**
     * The method assumes that at this point version & type have been parsed and are not a part of
     * the remaining transmission.
     */
    private static SinglePacketParseResult parseTypeFourPacket(int version, String remainingTransmission) {
        int pointer = 0;
        StringBuilder binValue = new StringBuilder();
        String group;
        do {
            group = remainingTransmission.substring(pointer, pointer + 5);
            binValue.append(group, 1, 5);
            pointer += 5;
        } while (group.charAt(0) != '0'); // last group condition
        long literalValue = binToDec(binValue.toString());
        Packet packet = new Packet(version, 4, literalValue, Collections.emptyList());
        if (pointer >= remainingTransmission.length()) {
            remainingTransmission = "";
        } else {
            remainingTransmission = remainingTransmission.substring(pointer);
        }
        return new SinglePacketParseResult(packet, remainingTransmission);
    }

    /**
     * The method assumes that at this point version & type have been parsed and are not a part of
     * the remaining transmission.
     */
    private static SinglePacketParseResult parseOperatorPacket(int version, int typeId, String remainingTransmission) {
        List<Packet> containedPackets = new ArrayList<>();
        char lengthTypeId = remainingTransmission.charAt(0);
        if (lengthTypeId == '0') {
            // length specified in bits
            int contentsTotalLengthInBits = (int) binToDec(
                    remainingTransmission.substring(1, 16)
            );
            String contentsBin = remainingTransmission.substring(16, 16 + contentsTotalLengthInBits);
            while (!contentsBin.isEmpty()) {
                SinglePacketParseResult parsedPacket = parseSinglePacket(contentsBin);
                containedPackets.add(parsedPacket.packet());
                contentsBin = parsedPacket.remainingBinTransmission();
            }
            remainingTransmission = remainingTransmission.substring(16 + contentsTotalLengthInBits);
        } else {
            // length specified as a number of sub-packets
            int numberOfSubPackets = (int) binToDec(
                    remainingTransmission.substring(1, 12)
            );
            remainingTransmission = remainingTransmission.substring(12);
            for (int i = 0; i < numberOfSubPackets; i++) {
                SinglePacketParseResult parsedPacket = parseSinglePacket(remainingTransmission);
                containedPackets.add(parsedPacket.packet());
                remainingTransmission = parsedPacket.remainingBinTransmission();
            }
        }
        return new SinglePacketParseResult(
                new Packet(version, typeId, -1, containedPackets),
                remainingTransmission
        );
    }

    private static SinglePacketParseResult parseSinglePacket(String binTransmission) {
        int version = (int) binToDec(binTransmission.substring(0, 3));
        int typeId = (int) binToDec(binTransmission.substring(3, 6));
        if (typeId == 4) {
            // literal value packet
            return parseTypeFourPacket(version, binTransmission.substring(6));
        } else {
            // operator packet
            return parseOperatorPacket(version, typeId, binTransmission.substring(6));
        }
    }

    /**
     * Parse the outermost packet in the transmission
     */
    public static Packet parsePacket(String hexTransmission) {
        String binTransmission = convertHexPacketToBinary(hexTransmission);
        return parseSinglePacket(binTransmission).packet();
    }

    public long getValue() {
        if (typeId == 4) {
            return literalValue;
        }
        LongStream valuesStream = containedPackets.stream()
                .mapToLong(Packet::getValue);
        return switch (typeId) {
            case 0 -> valuesStream.sum();
            case 1 -> valuesStream.reduce(1, (currProduct, newValue) -> currProduct * newValue);
            case 2 -> valuesStream.min().orElse(0);
            case 3 -> valuesStream.max().orElse(0);
            default -> {
                long firstPacketValue = containedPackets.get(0).getValue();
                long secondPacketValue = containedPackets.get(1).getValue();
                yield switch (typeId) {
                    case 5 -> firstPacketValue > secondPacketValue ? 1 : 0;
                    case 6 -> firstPacketValue < secondPacketValue ? 1 : 0;
                    case 7 -> firstPacketValue == secondPacketValue ? 1 : 0;
                    default -> throw new RuntimeException(
                            String.format("Invalid packet type of %d - cannot compute its value", typeId));
                };
            }
        };
    }
}

record SinglePacketParseResult(Packet packet, String remainingBinTransmission) {}
