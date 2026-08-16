package sk.tuke.kpi.kp.nonogram.mode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.nonogram.core.CellState;
import sk.tuke.kpi.kp.nonogram.core.Generate;
import sk.tuke.kpi.kp.nonogram.core.GameState;
import sk.tuke.kpi.kp.nonogram.core.mode.Game;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        // Сітка гри
        int[][] grid = {
                {1, 0},
                {0, 1}
        };

        // Підказки для рядків та стовпців
        var rowHints = java.util.Arrays.asList(java.util.List.of(1), java.util.List.of(1));
        var colHints = java.util.Arrays.asList(java.util.List.of(1), java.util.List.of(1));

        // Створення Nonogram з правильними підказками
        Generate.Nonogram nonogram = new Generate.Nonogram(grid, rowHints, colHints);

        // Створюємо гру з мінімальною логікою для тестів
        game = new Game(nonogram) {
            @Override
            protected void applyPenalty() {
                // Реалізація для тестування
                System.out.println("Penalty applied.");
            }

            @Override
            protected boolean checkFailCondition() {
                // Для тестів завжди повертатимемо false
                return false;
            }
        };
    }

    @Test
    public void testGameInitialization() {
        assertNotNull(game, "Game should be initialized");
    }

    @Test
    public void testMakeMoveCorrectCell() {
        game.makeMove(0, 0); // correct move (1)
        assertEquals(GameState.PLAYING, game.getState(), "Game should still be in playing state");
        assertEquals(CellState.CORRECT, game.getField().getUserGrid()[0][0], "Cell should be correctly revealed");
    }

    @Test
    public void testMakeMoveIncorrectCell() {
        game.makeMove(0, 1); // incorrect move (0)
        assertEquals(GameState.PLAYING, game.getState(), "Game should still be in playing state");
        assertEquals(CellState.FILLED, game.getField().getUserGrid()[0][1], "Cell should be marked as filled");
    }

    @Test
    public void testGameWinCondition() {
        game.makeMove(0, 0); // correct move (1)
        game.makeMove(1, 1); // correct move (1)
        assertEquals(GameState.SOLVED, game.getState(), "Game should be solved after correct moves");
    }

    @Test
    public void testInvalidMoveOutOfBounds() {
        game.makeMove(-1, 0); // invalid row index
        game.makeMove(0, -1); // invalid column index
        game.makeMove(2, 2); // out of bounds
        assertEquals(GameState.PLAYING, game.getState(), "Game should remain in playing state with invalid moves");
    }

    @Test
    public void testCellAlreadyOpened() {
        game.makeMove(0, 0); // correct move
        game.makeMove(0, 0); // trying to reveal the same cell
        assertEquals(GameState.PLAYING, game.getState(), "Game should remain in playing state when cell is already opened");
    }
}
