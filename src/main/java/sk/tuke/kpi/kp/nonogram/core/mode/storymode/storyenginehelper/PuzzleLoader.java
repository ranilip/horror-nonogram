package sk.tuke.kpi.kp.nonogram.core.mode.storymode.storyenginehelper;

import sk.tuke.kpi.kp.nonogram.consoleUI.PlayerSessionService;
import sk.tuke.kpi.kp.nonogram.core.Generate;
import sk.tuke.kpi.kp.nonogram.consoleUI.GameLoopUI;
import sk.tuke.kpi.kp.nonogram.core.mode.storymode.StoryMode;
import sk.tuke.kpi.kp.nonogram.service.exception.CommentException;
import sk.tuke.kpi.kp.nonogram.service.exception.RatingException;
import sk.tuke.kpi.kp.nonogram.service.exception.ScoreException;

import java.util.Scanner;

public class PuzzleLoader {
    public static void loadPuzzle(String line, PlayerSessionService session) throws ScoreException, RatingException, CommentException {
        line = line.replaceAll("[~\\u2028\\u00a0]", "").trim();

        int startMeta = line.indexOf("(");
        int endMeta = line.indexOf(")");
        String meta = (startMeta >= 0 && endMeta >= 0) ? line.substring(startMeta + 1, endMeta) : "";

        Integer lives = MetaExtractor.parseIntOrNull(MetaExtractor.getBetween(meta, "live:", "time:"));
        Integer time = MetaExtractor.parseIntOrNull(MetaExtractor.getAfter(meta, "time:"));

        String rest = line.substring(endMeta + 1).trim();
        String[] parts = rest.split(" ", 2);

        if (parts.length == 2) {
            int width = Integer.parseInt(parts[0]);
            String binary = parts[1].replaceAll("[^01]", "");

            if (width < 2 || binary.length() % width != 0) {
                System.out.println("⚠️ Invalid puzzle, skipping.");
                return;
            }

            var nonogram = Generate.fromBinary(binary, width);
            StoryMode storyMode = new StoryMode(nonogram, lives, time);

            GameLoopUI loop = new GameLoopUI(storyMode, new Scanner(System.in), session);
            loop.run();
        }
    }

}
