package sk.tuke.kpi.kp.nonogram.core;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Field {
    private final int rows;
    private final int cols;
    private final CellState[][] userGrid; // The user grid
    private final int[][] solutionGrid; // The solution grid

    @Setter
    private List<List<Integer>> rowHints;
    @Setter
    private List<List<Integer>> colHints;

    public Field(Generate.Nonogram nonogram) {
        this.rows = nonogram.grid().length;
        this.cols = nonogram.grid()[0].length;
        this.userGrid = new CellState[rows][cols];
        this.solutionGrid = nonogram.grid();
        this.rowHints = nonogram.rowHints();
        this.colHints = nonogram.colHints();
        closeCell();
    }

    // closed cells
    private void closeCell() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                userGrid[i][j] = CellState.CLOSE;
            }
        }
    }

    public boolean isSolved() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (solutionGrid[i][j] == 1 && userGrid[i][j] != CellState.CORRECT) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean reveal(int row, int col) {
        if (row < 0 || col < 0 || row >= rows || col >= cols) return false;

        if (solutionGrid[row][col] == 1) {
            userGrid[row][col] = CellState.CORRECT;
        } else {
            userGrid[row][col] = CellState.FILLED;
        }

        updateHints(); // оновлює підказки: змінює на 'X' якщо рядок/стовпчик завершено

        return solutionGrid[row][col] == 1;
    }

    //Hints = 'X' if the row/column is completed, otherwise the hints
    public void updateHints() {
        for (int r = 0; r < rows; r++) {
            List<Integer> hint = rowHints.get(r);
            List<Integer> updated = generatePartialHints(solutionGrid[r], userGrid[r], hint);
            rowHints.set(r, updated);
        }
        for (int c = 0; c < cols; c++) {
            int[] solCol = new int[rows];
            CellState[] usrCol = new CellState[rows];
            for (int r = 0; r < rows; r++) {
                solCol[r] = solutionGrid[r][c];
                usrCol[r] = userGrid[r][c];
            }
            List<Integer> hint = colHints.get(c);
            List<Integer> updated = generatePartialHints(solCol, usrCol, hint);
            colHints.set(c, updated);
        }
    }

    private List<Integer> generatePartialHints(int[] solution, CellState[] user, List<Integer> original) {
        List<Integer> result = new ArrayList<>();
        int idx = 0;
        int groupLength = 0;

        for (int i = 0; i <= solution.length; i++) {
            boolean isEnd = i == solution.length || solution[i] == 0;
            if (i < solution.length && solution[i] == 1) {
                groupLength++;
            }
            if (groupLength > 0 && isEnd) {
                boolean correct = true;
                for (int j = i - groupLength; j < i; j++) {
                    if (user[j] != CellState.CORRECT) {
                        correct = false;
                        break;
                    }
                }
                if (correct) {
                    result.add(-1); // X
                } else {
                    result.add(original.get(idx));
                }
                groupLength = 0;
                idx++;
            }
        }
        return result;
    }

}
