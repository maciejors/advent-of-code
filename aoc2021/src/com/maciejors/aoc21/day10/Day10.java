package com.maciejors.aoc21.day10;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

public class Day10 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("10", false);
        task12(input);
    }

    private static List<List<Bracket>> parseBrackets(List<String> input) {
        List<List<Bracket>> result = new ArrayList<>();
        for (String line : input) {
            result.add(
                    line.chars()
                            .mapToObj(c -> (char) c)
                            .map(Bracket::parseBracket)
                            .toList()
            );
        }
        return result;
    }

    private static void task12(List<String> input) {
        List<List<Bracket>> subsystem = parseBrackets(input);
        Map<BracketType, Integer> corruptedPointsMap = Map.of(
                BracketType.PARENTHESIS, 3,
                BracketType.SQUARE, 57,
                BracketType.CURLY, 1197,
                BracketType.POINTY, 25137
        );
        Map<BracketType, Integer> incompletePointsMap = Map.of(
                BracketType.PARENTHESIS, 1,
                BracketType.SQUARE, 2,
                BracketType.CURLY, 3,
                BracketType.POINTY, 4
        );
        int totalScoreCorrupted = 0;
        List<Long> scoresIncomplete = new ArrayList<>();

        outer: for (List<Bracket> line : subsystem) {
            Deque<BracketType> bracketsStack = new LinkedBlockingDeque<>();
            for (Bracket bracket : line) {
                if (bracket.isOpening()) {
                    bracketsStack.push(bracket.getType());
                    continue;
                }
                // closing bracket
                if (bracket.getType() == bracketsStack.peek()) {
                    bracketsStack.pop();
                } else {
                    // syntax error: wrong closing bracket
                    totalScoreCorrupted += corruptedPointsMap.get(bracket.getType());
                    continue outer;
                }
            }
            // if we reach this code it means the line is incomplete
            long lineScoreIncomplete = 0;
            for (BracketType bracketType : bracketsStack) {
                lineScoreIncomplete = lineScoreIncomplete * 5 + incompletePointsMap.get(bracketType);
            }
            scoresIncomplete.add(lineScoreIncomplete);
        }
        System.out.println("Task 1:");
        System.out.println(totalScoreCorrupted);
        System.out.println();

        // find the median of incomplete scores
        scoresIncomplete = scoresIncomplete.stream()
                .sorted()
                .toList();
        long finalScoreIncomplete = scoresIncomplete.get(scoresIncomplete.size() / 2);

        System.out.println("Task 2:");
        System.out.println(finalScoreIncomplete);
    }
}

enum BracketType {
    PARENTHESIS, SQUARE, CURLY, POINTY
}

class Bracket {
    private final char character;
    private final BracketType type;
    private final boolean isOpening;

    private Bracket(BracketType type, boolean isOpening, char character) {
        this.type = type;
        this.isOpening = isOpening;
        this.character = character;
    }

    public static Bracket parseBracket(char character) {
        return switch (character) {
            case '(' -> new Bracket(BracketType.PARENTHESIS, true, character);
            case ')' -> new Bracket(BracketType.PARENTHESIS, false, character);
            case '[' -> new Bracket(BracketType.SQUARE, true, character);
            case ']' -> new Bracket(BracketType.SQUARE, false, character);
            case '{' -> new Bracket(BracketType.CURLY, true, character);
            case '}' -> new Bracket(BracketType.CURLY, false, character);
            case '<' -> new Bracket(BracketType.POINTY, true, character);
            case '>' -> new Bracket(BracketType.POINTY, false, character);
            default -> null;
        };
    }

    public BracketType getType() {
        return type;
    }

    public boolean isOpening() {
        return isOpening;
    }

    @Override
    public String toString() {
        return Character.toString(character);
    }
}
