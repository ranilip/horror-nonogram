package sk.tuke.kpi.kp.nonogram.consoleUI;

import sk.tuke.kpi.kp.nonogram.core.CellState;
import sk.tuke.kpi.kp.nonogram.core.GameState;
import sk.tuke.kpi.kp.nonogram.core.mode.Game;
import sk.tuke.kpi.kp.nonogram.core.mode.ScoreMode;
import sk.tuke.kpi.kp.nonogram.core.mode.storymode.StoryMode;

import java.util.List;
import java.util.stream.Collectors;

public class PrintUI {

    public static void printRowHints(Game game) {
        System.out.println("\nRow hints:");
        List<List<Integer>> rowHints = game.getField().getRowHints();
        for (int r = 0; r < rowHints.size(); r++) {
            String hintStr = rowHints.get(r).stream()
                    .map(h -> h == -1 ? "X" : String.valueOf(h))
                    .collect(Collectors.joining(", "));
            System.out.printf("%2d: %s%n", r + 1, hintStr.isEmpty() ? "0" : hintStr);
        }
    }

    public static void printColumnHints(Game game) {
        System.out.println("Column hints:");
        List<List<Integer>> colHints = game.getField().getColHints();
        for (int c = 0; c < colHints.size(); c++) {
            String hintStr = colHints.get(c).stream()
                    .map(h -> h == -1 ? "X" : String.valueOf(h))
                    .collect(Collectors.joining(", "));
            System.out.printf(" %d: %s%n", c + 1, hintStr.isEmpty() ? "0" : hintStr);
        }
    }

    public static void printField(Game game) {
        System.out.println("\nField:");
        System.out.print("   ");
        for (int c = 0; c < game.getField().getCols(); c++) {
            System.out.printf("%2d ", c + 1);
        }
        System.out.println();

        CellState[][] grid = game.getField().getUserGrid();
        for (int r = 0; r < grid.length; r++) {
            System.out.printf("%2d  ", r + 1);
            for (CellState cell : grid[r]) {
                System.out.print(cell.toSymbol() + " ");
            }
            System.out.println();
        }
    }

    //-------------------------------------------------------------------------

    public static void printGameResult(Game game) {
        if (game.getState() == GameState.SOLVED) {
            printSolved(game);
        } else if (game.getState() == GameState.FAILED) {
            printFailed(game);
        } else {
            System.out.println("❔ Game is still in progress.");
        }
    }

    public static void printHeader(Game game) {
        if (game instanceof ScoreMode scoreMode) {
            System.out.println("\n" + "❤️".repeat(scoreMode.getLives()));
        }
        else if (game instanceof StoryMode storyMode) {
            if (storyMode.getLives() != null) {
                System.out.println("\n" + "❤️".repeat(storyMode.getLives()));
            }
            if (storyMode.getTimeLimit() != null) {
                System.out.println("\n" + "⏳:" + storyMode.getRemainingTimeSeconds());
            }
        }
    }

    public static void printSolved(Game game) {
        System.out.println("\nPuzzle completed!\n");
        if (game instanceof ScoreMode scoreMode) {
            System.out.println("⭐ Score: " + scoreMode.getCurrentScore());
            System.out.println("🏆 Total Score: " + scoreMode.getTotalScore());
        }
    }

    public static void printFailed(Game game) {
        System.out.println("\n💀 Game over!");
        if (game instanceof ScoreMode scoreMode) {
            System.out.println("💯 Final Score: " + scoreMode.getTotalScore());
        }
    }

    public static void printStart() {
        System.out.println("""
                 _________  __              _____      _         __       _   __                                   \s
                |  _   _  |[  |            |_   _|    (_)       [  |     / |_[  |                                  \s
                |_/ | | \\_| | |--.  .---.    | |      __   .--./)| |--. `| |-'| |--.   .--.   __   _   .--.  .---. \s
                    | |     | .-. |/ /__\\\\   | |   _ [  | / /'`\\;| .-. | | |  | .-. |/ .'`\\ \\[  | | | ( (`\\]/ /__\\\\\s
                   _| |_    | | | || \\__.,  _| |__/ | | | \\ \\._//| | | | | |, | | | || \\__. | | \\_/ |, `'.'.| \\__.,\s
                  |_____|  [___]|__]'.__.' |________|[___].',__`[___]|__]\\__/[___]|__]'.__.'  '.__.'_/[\\__) )'.__.'\s
                                                         ( ( __))   \s
                
                """);
    }
}

