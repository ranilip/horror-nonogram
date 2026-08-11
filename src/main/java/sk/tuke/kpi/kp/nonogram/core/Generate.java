package sk.tuke.kpi.kp.nonogram.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generate {

    public static Nonogram fromBinary(String binary, int rowLength) {
        int rows = binary.length() / rowLength;
        int[][] grid = new int[rows][rowLength];

        for (int i = 0; i < binary.length(); i++) {
            grid[i / rowLength][i % rowLength] = binary.charAt(i) == '1' ? 1 : 0;
        }

        return new Nonogram(grid, generateHints(grid, true), generateHints(grid, false));
    }

    public record Nonogram(int[][] grid, List<List<Integer>> rowHints, List<List<Integer>> colHints) {}

    public static Nonogram convert(int minRows, int maxRows, int minCols, int maxCols) {
        return generateRandom(minRows, maxRows, minCols, maxCols);
    }

    private static Nonogram generateRandom(int minRows, int maxRows, int minCols, int maxCols) {
        Random random = new Random();
        int rows = random.nextInt(maxRows - minRows + 1) + minRows;
        int cols = random.nextInt(maxCols - minCols + 1) + minCols;

        int[][] grid = new int[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                grid[y][x] = random.nextBoolean() ? 1 : 0;
            }
        }
        grid[random.nextInt(rows)][random.nextInt(cols)] = 1;

        return new Nonogram(grid, generateHints(grid, true), generateHints(grid, false));
    }

    public static List<List<Integer>> generateHints(int[][] grid, boolean isRow) {
        int outer = isRow ? grid.length : grid[0].length;
        int inner = isRow ? grid[0].length : grid.length;
        List<List<Integer>> hints = new ArrayList<>();

        for (int i = 0; i < outer; i++) {
            List<Integer> lineHints = new ArrayList<>();
            int count = 0;

            for (int j = 0; j < inner; j++) {
                int cell = isRow ? grid[i][j] : grid[j][i];
                if (cell == 1) {
                    count++;
                } else if (count > 0) {
                    lineHints.add(count);
                    count = 0;
                }
            }

            if (count > 0) {
                lineHints.add(count);
            }

            if (lineHints.isEmpty()) {
                lineHints.add(0);
            }

            hints.add(lineHints);
        }

        return hints;
    }
}
