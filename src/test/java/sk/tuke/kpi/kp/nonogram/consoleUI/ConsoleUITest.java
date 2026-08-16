package sk.tuke.kpi.kp.nonogram.consoleUI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.nonogram.core.mode.Mode;
import sk.tuke.kpi.kp.nonogram.service.JPA.CommentServiceJPA;
import sk.tuke.kpi.kp.nonogram.service.JPA.RatingServiceJPA;
import sk.tuke.kpi.kp.nonogram.service.JPA.ScoreServiceJPA;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleUITest {

    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUp() {
        // Нічого не треба ініціалізовувати
    }

    @AfterEach
    public void restoreSystemInput() {
        System.setIn(originalIn);
    }

    @Test
    public void testChooseScoreMode() throws Exception {
        String simulatedInput = "1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ConsoleUI consoleUI = new ConsoleUI(fakeSessionService());
        Mode mode = invokeChooseMode(consoleUI);
        assertEquals(Mode.SCOREMODE, mode);
    }

    @Test
    public void testChooseStoryMode() throws Exception {
        String simulatedInput = "2\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ConsoleUI consoleUI = new ConsoleUI(fakeSessionService());
        Mode mode = invokeChooseMode(consoleUI);
        assertEquals(Mode.STORYMODE, mode);
    }

    private Mode invokeChooseMode(ConsoleUI consoleUI) throws Exception {
        var method = ConsoleUI.class.getDeclaredMethod("chooseMode");
        method.setAccessible(true);
        return (Mode) method.invoke(consoleUI);
    }

    private PlayerSessionService fakeSessionService() {
        PlayerSessionService session = new PlayerSessionService(
                new ScoreServiceJPA(),
                new CommentServiceJPA(),
                new RatingServiceJPA()
        );
        session.setSessionContext(new Scanner(System.in), "test-nonogram");
        return session;
    }
}
