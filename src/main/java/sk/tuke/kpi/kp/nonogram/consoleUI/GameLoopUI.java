package sk.tuke.kpi.kp.nonogram.consoleUI;

import sk.tuke.kpi.kp.nonogram.core.GameState;
import sk.tuke.kpi.kp.nonogram.core.mode.Game;
import sk.tuke.kpi.kp.nonogram.core.mode.ScoreMode;
import sk.tuke.kpi.kp.nonogram.core.mode.storymode.Print;
import sk.tuke.kpi.kp.nonogram.core.mode.storymode.StoryMode;

import java.util.Scanner;

public class GameLoopUI {
    private final Game game;
    private final Scanner scanner;
    private final PlayerSessionService session;
    boolean running = true;

    public GameLoopUI(Game game, Scanner scanner, PlayerSessionService session) {
        this.game = game;
        this.scanner = scanner;
        this.session = session;
    }

    public void run() {
        while (running) {
            show();

            if (game instanceof ScoreMode scoreMode) {
                handleScoreMode(scoreMode);
            } else if (game instanceof StoryMode storyMode) {
                handleStoryMode(storyMode);
            } else {
                handleDefaultMode();
            }
        }
    }

    private void show() {
        while (game.getState() == GameState.PLAYING) {
            PrintUI.printRowHints(game);
            PrintUI.printColumnHints(game);
            PrintUI.printField(game);
            PrintUI.printHeader(game);

            handleInput();
        }
    }

    private void handleInput() {
        System.out.print("\nRow : ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("skip") || input.equalsIgnoreCase("s")) {
            game.setState(GameState.SOLVED);
            return;
        }

        int row;
        try {
            row = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid row input. Enter a valid number.");
            return;
        }

        System.out.print("Column : ");
        String colInput = scanner.nextLine().trim();

        int col;
        try {
            col = Integer.parseInt(colInput) - 1;
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid column input. Enter a valid number.");
            return;
        }

        game.makeMove(row, col);
    }

    private void handleSolved(ScoreMode scoreMode) {
        scoreMode.reward();
        scoreMode.resetPuzzle();
    }

    private void handleFailed(ScoreMode scoreMode) {
        PrintUI.printFailed(scoreMode);
    }

    private void handleScoreMode(ScoreMode scoreMode) {
        if (game.getState() == GameState.SOLVED) {
            handleSolved(scoreMode);
        } else if (game.getState() == GameState.FAILED) {
            handleFailed(scoreMode);
            session.initPlayer();
            session.saveScore(scoreMode.getTotalScore());
            session.requestRating();
            session.requestComment();
            session.getAverageRating();
            session.getTopScores();
            session.showPreviousComments();
            running = false;
        }
    }

    private void handleStoryMode(StoryMode storyMode) {
        if (game.getState() == GameState.FAILED) {
            PrintUI.printFailed(storyMode);
            Print.showEnding();
            session.initPlayer();
            session.requestRating();
            session.requestComment();
            session.showPreviousComments();
            running = false;
        } else if (game.getState() == GameState.SOLVED) {
            PrintUI.printGameResult(storyMode);
            running = false;
        }
    }

    private void handleDefaultMode() {
        PrintUI.printGameResult(game);
        running = false;
    }
}
