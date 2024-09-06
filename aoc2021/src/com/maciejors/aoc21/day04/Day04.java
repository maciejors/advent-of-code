package com.maciejors.aoc21.day04;

import com.maciejors.aoc21.shared.CommonFunctions;
import com.maciejors.aoc21.shared.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day04 {
    public static void main(String[] args) {
        List<String> lines = CommonFunctions.readInput("04", false);
        System.out.println("Task 1:");
        task1(lines);
        System.out.println();
        System.out.println("Task 2:");
        task2(lines);
    }

    private static Bingo parseBingo(List<String> input) {
        // parse numbers
        List<Integer> numbers = Arrays.stream(input.get(0).split(","))
                .map(Integer::parseInt)
                .toList();
        // parse boards
        List<Board> boards = new ArrayList<>();
        for (int i = 2; i < input.size(); i += 6) {
            List<String> boardRaw = input.subList(i, i + 5);
            List<List<Integer>> boardContentsList = boardRaw
                    .stream()
                    .map(s -> Arrays.stream(s.trim().split("\\s+"))
                            .map(Integer::parseInt)
                            .toList())
                    .toList();
            Matrix<Integer> boardContents = new Matrix<>(boardContentsList);
            boards.add(new Board(boardContents));
        }
        return new Bingo(numbers, boards);
    }

    private static void task1(List<String> lines) {
        Bingo bingo = parseBingo(lines);
        Board winningBoard = null;
        outer: for (int number: bingo.numbers()) {
            for (Board board : bingo.boards()) {
                boolean isBoardWinning = board.handleDrawnNumber(number);
                if (isBoardWinning) {
                    winningBoard = board;
                    break outer;
                }
            }
        }
        if (winningBoard == null) {
            System.out.println("No board won");
            return;
        }
        System.out.println(winningBoard.getScore());
    }

    private static void task2(List<String> lines) {
        Bingo bingo = parseBingo(lines);

        final int boardsCount = bingo.boards().size();
        List<Integer> winningBoardIndexes = getWinningBoardIndexesInOrder(bingo, boardsCount);
        if (winningBoardIndexes.isEmpty()) {
            System.out.println("No board won");
            return;
        }
        int lastWinningBoardIdx = winningBoardIndexes.get(winningBoardIndexes.size() - 1);
        Board lastWinningBoard = bingo.boards().get(lastWinningBoardIdx);
        System.out.println(lastWinningBoard.getScore());
    }

    private static List<Integer> getWinningBoardIndexesInOrder(Bingo bingo, int boardsCount) {
        List<Integer> winningBoardIndexes = new LinkedList<>();

        for (int number: bingo.numbers()) {
            for (int i = 0; i < boardsCount; i++) {
                // don't check boards which already won
                if (winningBoardIndexes.contains(i)) {
                    continue;
                }
                Board board = bingo.boards().get(i);
                boolean isBoardWinning = board.handleDrawnNumber(number);
                if (isBoardWinning) {
                    winningBoardIndexes.add(i);
                }
            }
        }
        return winningBoardIndexes;
    }
}