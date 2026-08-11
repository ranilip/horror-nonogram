package sk.tuke.kpi.kp.nonogram.core.mode.storymode.storyenginehelper;

import sk.tuke.kpi.kp.nonogram.consoleUI.PlayerSessionService;
import sk.tuke.kpi.kp.nonogram.core.mode.storymode.Print;
import sk.tuke.kpi.kp.nonogram.service.exception.CommentException;
import sk.tuke.kpi.kp.nonogram.service.exception.RatingException;
import sk.tuke.kpi.kp.nonogram.service.exception.ScoreException;

import java.util.Scanner;

public class StoryParser {
    private final Scanner scanner;
    private final PlayerSessionService sessionService;

    public StoryParser(Scanner scanner, PlayerSessionService sessionService) {
        this.scanner = scanner;
        this.sessionService = sessionService;
    }

    public void processLine(String raw) throws ScoreException, RatingException, CommentException {
        String line = raw.replaceAll("\\p{C}&&[^\\n]", "").trim();
        if (line.isBlank()) return;

        if (line.startsWith("=")) {
            Print.separator();
        } else if (line.matches("\\*.*\\*")) {
            System.out.println(line.replace("*", "").trim());
            scanner.nextLine();
        } else if (line.matches("\\(.*\\)«.*»")) {
            String emotion = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
            String text = line.substring(line.indexOf('«') + 1, line.lastIndexOf('»'));
            Print.character(emotion, text);
        } else if (line.startsWith("~")) {
            PuzzleLoader.loadPuzzle(line, sessionService);
        }
    }
}

