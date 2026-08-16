package sk.tuke.kpi.kp.nonogram.mode;

import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.nonogram.core.Generate;
import sk.tuke.kpi.kp.nonogram.core.mode.Game;
import sk.tuke.kpi.kp.nonogram.core.mode.GameFactory;
import sk.tuke.kpi.kp.nonogram.core.mode.Mode;

import static org.junit.jupiter.api.Assertions.*;

public class GameFactoryTest {

    @Test
    public void testCreateScoreModeGame() {
        int[][] grid = {
                {1, 0},
                {0, 1}
        };
        Generate.Nonogram nonogram = new Generate.Nonogram(grid, null, null);

        Game game = GameFactory.createGame(Mode.SCOREMODE, nonogram);
        assertNotNull(game, "Game should be created for SCOREMODE");
    }

    @Test
    public void testCreateStoryModeGame() {
        int[][] grid = {
                {1, 0},
                {0, 1}
        };
        Generate.Nonogram nonogram = new Generate.Nonogram(grid, null, null);

        Game game = GameFactory.createGame(Mode.STORYMODE, nonogram);
        assertNotNull(game, "Game should be created for STORYMODE");
    }
}

