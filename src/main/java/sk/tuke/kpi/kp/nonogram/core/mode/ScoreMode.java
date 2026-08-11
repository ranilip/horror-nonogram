package sk.tuke.kpi.kp.nonogram.core.mode;

import lombok.Getter;
import sk.tuke.kpi.kp.nonogram.consoleUI.PrintUI;
import sk.tuke.kpi.kp.nonogram.core.CellState;
import sk.tuke.kpi.kp.nonogram.core.Field;
import sk.tuke.kpi.kp.nonogram.core.GameState;
import sk.tuke.kpi.kp.nonogram.core.Generate;

@Getter
public class ScoreMode extends Game {
    private int lives = 3;
    private int totalScore = 0;
    private int currentScore = 0;

    public ScoreMode(Generate.Nonogram nonogram) {
        super(nonogram);
    }

    @Override
    protected void applyPenalty() {
        lives--;
        currentScore -= 50;
        System.out.println("\n❌ Wrong move! Lives left: " + "❤️".repeat(lives));
    }

    @Override
    protected boolean checkFailCondition() {
        return lives <= 0;
    }

    public void reward() {
        int count = countCorrectCells();
        int scoreThisRound = count * 250;
        currentScore += scoreThisRound;
        totalScore += currentScore;
        PrintUI.printSolved(this);
        currentScore = 0;
    }

    private int countCorrectCells() {
        int count = 0;
        CellState[][] userGrid = field.getUserGrid();
        int[][] solution = field.getSolutionGrid();

        for (int i = 0; i < userGrid.length; i++) {
            for (int j = 0; j < userGrid[0].length; j++) {
                if (solution[i][j] == 1 && userGrid[i][j] == CellState.CORRECT) {
                    count++;
                }
            }
        }
        return count;
    }

    public void resetPuzzle() {
        var result = Generate.convert(2, 3, 2, 3);
        this.field = new Field(result);
        this.state = GameState.PLAYING;
    }
}
