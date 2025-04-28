package com.example.sudoku;

import java.util.Arrays;
import java.util.Random;

public class Sudoku {
    private static final int SIZE = 9;
    private static final int SUBGRID = 3;
    public int[][] grid; // Make this public to access it from the controller
    private Random random;

    public Sudoku() {
        grid = new int[SIZE][SIZE];
        random = new Random();
    }

    private boolean isValid(int row, int col, int num) {
        for (int x = 0; x < SIZE; x++) {
            if (grid[row][x] == num || grid[x][col] == num) {
                return false;
            }
        }
        int startRow = row - row % SUBGRID, startCol = col - col % SUBGRID;
        for (int i = 0; i < SUBGRID; i++) {
            for (int j = 0; j < SUBGRID; j++) {
                if (grid[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean solveSudoku() {
        int[] empty = findEmptyLocation();
        if (empty == null) {
            return true;
        }
        int row = empty[0], col = empty[1];
        for (int num = 1; num <= SIZE; num++) {
            if (isValid(row, col, num)) {
                grid[row][col] = num;
                if (solveSudoku()) {
                    return true;
                }
                grid[row][col] = 0;
            }
        }
        return false;
    }

    private int[] findEmptyLocation() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private void removeNumbers(String difficulty) {
        int squaresToRemove;
        switch (difficulty) {
            case "medium":
                squaresToRemove = random.nextInt(7) + 47; // 47 to 53
                break;
            case "hard":
                squaresToRemove = random.nextInt(7) + 54; // 54 to 60
                break;
            case "easy":
            default:
                squaresToRemove = random.nextInt(7) + 40; // 40 to 46
                break;
        }

        while (squaresToRemove > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (grid[row][col] != 0) {
                grid[row][col] = 0;
                squaresToRemove--;
            }
        }
    }

    public void generateSudoku(String difficulty) {
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(grid[i], 0);
        }

        for (int i = 0; i < SIZE; i += SUBGRID) {
            int[] numList = random.ints(1, SIZE + 1).distinct().limit(SIZE).toArray();
            for (int j = 0; j < SIZE; j++) {
                grid[i + j / SUBGRID][i + j % SUBGRID] = numList[j];
            }
        }
        solveSudoku();
        removeNumbers(difficulty);
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[][] getGridCopy() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    public boolean solveSudokuWithSteps(int[][] steps, int[] stepIndex) {
        int[] empty = findEmptyLocation();
        if (empty == null) {
            return true;
        }
        int row = empty[0], col = empty[1];
        for (int num = 1; num <= SIZE; num++) {
            if (isValid(row, col, num)) {
                grid[row][col] = num;
                steps[stepIndex[0]++] = new int[]{row, col, num}; // Store the step
                if (solveSudokuWithSteps(steps, stepIndex)) {
                    return true;
                }
                grid[row][col] = 0;
                steps[stepIndex[0]++] = new int[]{row, col, 0}; // Store the backtrack step
            }
        }
        return false;
    }
}
