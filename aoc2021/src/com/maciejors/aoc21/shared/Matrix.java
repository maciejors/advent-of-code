package com.maciejors.aoc21.shared;

import java.util.List;

/**
 * A wrapper for 2D arrays with some useful methods
 */
public class Matrix<T> {
    private final T[][] values;
    /**
     * A [height, width] tuple
     */
    private final int[] shape;

    /**
     * @param values matrix values
     * @param immediateSet if true, values will not be processed. Otherwise, a copy of array will
     *                     be made, and checks will be performed on arrays shape.
     */
    private Matrix(T[][] values, boolean immediateSet) {
        int height = values.length;
        int width = height == 0 ? 0 : values[0].length;
        shape = new int[]{height, width};
        if (immediateSet) {
            this.values = values;
        } else {
            @SuppressWarnings("unchecked")
            T[][] valuesCopy = (T[][]) new Object[height][width];
            for (int rowIdx = 0; rowIdx < height; rowIdx++) {
                T[] row = values[rowIdx];
                if (row.length != width) {
                    throw new IllegalArgumentException("Lists in `values` have varying lengths");
                }
                valuesCopy[rowIdx] = values[rowIdx].clone();
            }
            this.values = valuesCopy;
        }
    }

    public Matrix(T[][] values) {
        this(values, false);
    }

    public Matrix(List<List<T>> values) {
        int height = values.size();
        int width = height == 0 ? 0 : values.get(0).size();
        shape = new int[]{height, width};
        // copy values
        Object[][] valuesRaw = new Object[height][width];
        for (int i = 0; i < height; i++) {
            List<T> row = values.get(i);
            Object[] rowArray = row.toArray();
            // validate shape
            if (rowArray.length != width) {
                throw new IllegalArgumentException("Lists in `values` have varying lengths");
            }
            valuesRaw[i] = rowArray;
        }
        @SuppressWarnings("unchecked")
        T[][] valuesCast = (T[][]) valuesRaw;
        this.values = valuesCast;
    }

    public static <T> Matrix<T> full(int height, int width, T value) {
        @SuppressWarnings("unchecked")
        T[][] values = (T[][]) new Object[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                values[i][j] = value;
            }
        }
        return new Matrix<>(values, true);
    }

    public void set(int rowIdx, int colIdx, T value) {
        values[rowIdx][colIdx] = value;
    }

    public T get(int rowIdx, int colIdx) {
        return values[rowIdx][colIdx];
    }

    /**
     * Retrieve a copy of a row
     */
    public T[] peekRow(int i) {
        return values[i].clone();
    }

    public T[] peekColumn(int i) {
        int height = shape[0];
        @SuppressWarnings("unchecked")
        T[] column = (T[]) new Object[height];
        for (int j = 0; j < height; j++) {
            column[j] = values[j][i];
        }
        return column;
    }

    /**
     * A [height, width] tuple
     */
    public int[] getShape() {
        return shape.clone();
    }
}
