package com.example.sudoku;

import org.springframework.web.bind.annotation.*;
import java.util.Arrays;

@RestController
public class SudokuController {

    private Sudoku sudoku = new Sudoku();

    @GetMapping("/generate-sudoku")
    public int[][] generateSudoku(@RequestParam(defaultValue = "easy") String difficulty) {
        sudoku.generateSudoku(difficulty);
        return sudoku.getGrid();
    }

    @PostMapping("/solve-sudoku")
    public int[][] solveSudoku(@RequestBody int[][] puzzle) {
        sudoku = new Sudoku();
        sudoku.grid = puzzle;
        int[][] steps = new int[81 * 9][3];
        int[] stepIndex = {0};
        sudoku.solveSudokuWithSteps(steps, stepIndex);
        return Arrays.copyOf(steps, stepIndex[0]); // Return only the filled part of the steps array
    }
}
