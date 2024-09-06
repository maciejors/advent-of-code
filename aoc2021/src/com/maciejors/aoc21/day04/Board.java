package com.maciejors.aoc21.day04;

import com.maciejors.aoc21.shared.Matrix;

public class Board {
    private final Matrix<Integer> boardNumbers;
    private final Matrix<Boolean> markedFlags;
    private final int size;
    private int score = 0;

    public Board(Matrix<Integer> boardNumbers) {
        int[] shape = boardNumbers.getShape();
        if (shape[0] != shape[1]) {
            throw new IllegalArgumentException("Board must be square");
        }
        this.size = shape[0];
        this.boardNumbers = boardNumbers;
        markedFlags = Matrix.full(size, size, false);
    }

    /**
     * @return Whether the board is now winning
     */
    public boolean handleDrawnNumber(int drawnNumber) {
        // find the location of the number
        int foundNumberRowIdx = -1;
        int foundNumberColIdx = -1;
        outer: for (int rowIdx = 0; rowIdx < size; rowIdx++) {
            for (int colIdx = 0; colIdx < size; colIdx++) {
                int number = boardNumbers.get(rowIdx, colIdx);
                if (number == drawnNumber) {
                    markedFlags.set(rowIdx, colIdx, true);
                    foundNumberRowIdx = rowIdx;
                    foundNumberColIdx = colIdx;
                    break outer;
                }
            }
        }
        // number not on board so definitely not won now
        if (foundNumberRowIdx == -1) {
            return false;
        }
        // check if won (can only win now in the row/col of the drawn number)
        boolean wonInRow = true;
        boolean wonInCol = true;
        for (int i = 0; i < size; i++) {
            boolean flagInRow = markedFlags.get(foundNumberRowIdx, i);
            if (!flagInRow) {
                wonInRow = false;
            }
            boolean flagInCol = markedFlags.get(i, foundNumberColIdx);
            if (!flagInCol) {
                wonInCol = false;
            }
        }
        if (wonInRow || wonInCol) {
            score = calculateScore(drawnNumber);
            return true;
        }
        return false;
    }

    private int calculateScore(int lastDrawnNumber) {
        int sumOfUnmarked = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boolean isMarked = markedFlags.get(i, j);
                if (!isMarked) {
                    sumOfUnmarked += boardNumbers.get(i, j);
                }
            }
        }
        return sumOfUnmarked * lastDrawnNumber;
    }

    public int getScore() {
        return score;
    }
}
