package sk.tuke.kpi.kp.nonogram.mode;

import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.nonogram.core.mode.Mode;

import static org.junit.jupiter.api.Assertions.*;

public class ModeTest {

    @Test
    public void testModeValues() {
        assertEquals(Mode.SCOREMODE, Mode.valueOf("SCOREMODE"));
        assertEquals(Mode.STORYMODE, Mode.valueOf("STORYMODE"));
    }

    @Test
    public void testModeToString() {
        assertEquals("SCOREMODE", Mode.SCOREMODE.toString());
        assertEquals("STORYMODE", Mode.STORYMODE.toString());
    }
}
