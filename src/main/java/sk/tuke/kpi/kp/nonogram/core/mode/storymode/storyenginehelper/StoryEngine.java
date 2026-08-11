package sk.tuke.kpi.kp.nonogram.core.mode.storymode.storyenginehelper;

import sk.tuke.kpi.kp.nonogram.consoleUI.PlayerSessionService;
import sk.tuke.kpi.kp.nonogram.core.mode.storymode.Print;
import sk.tuke.kpi.kp.nonogram.service.JPA.CommentServiceJPA;
import sk.tuke.kpi.kp.nonogram.service.JPA.RatingServiceJPA;
import sk.tuke.kpi.kp.nonogram.service.JPA.ScoreServiceJPA;
import sk.tuke.kpi.kp.nonogram.service.exception.CommentException;
import sk.tuke.kpi.kp.nonogram.service.exception.RatingException;
import sk.tuke.kpi.kp.nonogram.service.exception.ScoreException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
public class StoryEngine {
    Scanner scanner = new Scanner(System.in);

    PlayerSessionService playerSessionService = new PlayerSessionService(
            new ScoreServiceJPA(),
            new CommentServiceJPA(),
            new RatingServiceJPA()
    );
    {
        playerSessionService.setSessionContext(scanner, "nonogram");
    }

    public StoryEngine(){}

    public void run(String storyFilePath) throws IOException, ScoreException, RatingException, CommentException {
        Print.showIntro();

        List<String> lines = Files.readAllLines(Paths.get(storyFilePath));
        Scanner scanner = new Scanner(System.in);

        StoryParser parser = new StoryParser(scanner, playerSessionService);

        for (String raw : lines) {
            parser.processLine(raw);
        }

        Print.showEnding();
        playerSessionService.initPlayer();
        playerSessionService.requestRating();
        playerSessionService.requestComment();
        playerSessionService.showPreviousComments();
        System.exit(0);
    }
}
