package sk.tuke.kpi.kp.nonogram.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.nonogram.core.CellState;
import sk.tuke.kpi.kp.nonogram.core.Field;
import sk.tuke.kpi.kp.nonogram.core.Generate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private Field field;

    @BeforeEach
    public void setUp() {
        // Створимо просту 3x3 сітку з ручними підказками
        int[][] solution = {
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}
        };
        List<List<Integer>> rowHints = Arrays.asList(
                List.of(1, 1),
                List.of(1),
                List.of(1, 1)
        );
        List<List<Integer>> colHints = Arrays.asList(
                List.of(1, 1),
                List.of(1),
                List.of(1, 1)
        );
        Generate.Nonogram nonogram = new Generate.Nonogram(solution, rowHints, colHints);
        field = new Field(nonogram);
    }

    @Test
    public void testInitialGridIsClosed() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                assertEquals(CellState.CLOSE, field.getUserGrid()[r][c],
                        "Initial cell should be CLOSE");
            }
        }
    }

    @Test
    public void testRevealCorrectCell() {
        boolean result = field.reveal(0, 0); // в розв'язку стоїть 1
        assertTrue(result);
        assertEquals(CellState.CORRECT, field.getUserGrid()[0][0]);
    }

    @Test
    public void testRevealIncorrectCell() {
        boolean result = field.reveal(0, 1); // в розв'язку стоїть 0
        assertFalse(result);
        assertEquals(CellState.FILLED, field.getUserGrid()[0][1]);
    }

    @Test
    public void testIsSolvedFalseAtStart() {
        assertFalse(field.isSolved());
    }

    @Test
    public void testIsSolvedTrueAfterRevealingAllCorrect() {
        field.reveal(0, 0);
        field.reveal(0, 2);
        field.reveal(1, 1);
        field.reveal(2, 0);
        field.reveal(2, 2);
        assertTrue(field.isSolved());
    }
}
