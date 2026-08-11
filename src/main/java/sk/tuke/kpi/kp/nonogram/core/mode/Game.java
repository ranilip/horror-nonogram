package sk.tuke.kpi.kp.nonogram.core.mode;

import lombok.Getter;
import lombok.Setter;
import sk.tuke.kpi.kp.nonogram.core.CellState;
import sk.tuke.kpi.kp.nonogram.core.Field;
import sk.tuke.kpi.kp.nonogram.core.GameState;
import sk.tuke.kpi.kp.nonogram.core.Generate;

@Getter
@Setter
public abstract class Game {
    protected Field field;
    protected GameState state = GameState.PLAYING;
    protected GameState game = GameState.PLAYING;

    public Game(Generate.Nonogram nonogram) {
        this.field = new Field(nonogram);
    }

    public void makeMove(int row, int col) {
        if (row < 0 || row >= field.getRows() || col < 0 || col >= field.getCols()) {
            System.out.println("\nInvalid coordinates!");
            return;
        }

        if (field.getUserGrid()[row][col] != CellState.CLOSE) {
            System.out.println("\nThis cell is already opened. Choose another one.");
            return;
        }

        boolean isCorrect = field.reveal(row, col);
        field.updateHints();
        if (!isCorrect) applyPenalty();

        if (field.isSolved()) {
            state = GameState.SOLVED;
        } else if (checkFailCondition()) {
            state = GameState.FAILED;
        }
    }

    protected abstract void applyPenalty();
    protected abstract boolean checkFailCondition();
}

