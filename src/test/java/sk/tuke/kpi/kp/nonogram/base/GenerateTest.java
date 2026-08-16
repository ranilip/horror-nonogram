package sk.tuke.kpi.kp.nonogram.base;

import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.nonogram.core.Generate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateTest {

    @Test
    public void testFromBinaryCreatesCorrectGrid() {
        String binary = "101010000";
        int[][] expectedGrid = {
                {1, 0, 1},
                {0, 1, 0},
                {0, 0, 0}
        };
        Generate.Nonogram nonogram = Generate.fromBinary(binary, 3);
        int[][] actualGrid = nonogram.grid();

        assertEquals(3, actualGrid.length);
        assertEquals(3, actualGrid[0].length);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedGrid[i][j], actualGrid[i][j]);
            }
        }
    }

    @Test
    public void testGenerateHintsRows() {
        int[][] grid = {
                {1, 0, 1},
                {1, 1, 0},
                {0, 0, 0}
        };
        List<List<Integer>> hints = Generate.generateHints(grid, true);
        assertEquals(List.of(List.of(1, 1), List.of(2), List.of(0)), hints);
    }

    @Test
    public void testGenerateHintsCols() {
        int[][] grid = {
                {1, 0, 1},
                {1, 1, 0},
                {0, 0, 0}
        };
        List<List<Integer>> hints = Generate.generateHints(grid, false);
        assertEquals(List.of(List.of(2), List.of(1), List.of(1)), hints);
    }

    @Test
    public void testGenerateRandomReturnsValidGrid() {
        Generate.Nonogram nonogram = Generate.convert(5, 5, 5, 5);
        int[][] grid = nonogram.grid();
        assertEquals(5, grid.length);
        assertEquals(5, grid[0].length);

        boolean hasAtLeastOneFilled = false;
        for (int[] row : grid) {
            for (int val : row) {
                if (val == 1) {
                    hasAtLeastOneFilled = true;
                    break;
                }
            }
        }
        assertTrue(hasAtLeastOneFilled, "Grid should contain at least one filled cell");
    }
}
