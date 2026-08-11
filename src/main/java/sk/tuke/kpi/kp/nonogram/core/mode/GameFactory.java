package sk.tuke.kpi.kp.nonogram.core.mode;

import sk.tuke.kpi.kp.nonogram.core.Generate;
import sk.tuke.kpi.kp.nonogram.core.mode.storymode.storyenginehelper.StoryEngine;

public class GameFactory {

    public static Game createGame(Mode mode, Generate.Nonogram nonogram) {
        return switch (mode) {
            case SCOREMODE -> new ScoreMode(nonogram);
            case STORYMODE -> {
                try {
                    new StoryEngine().run("Nonogram/src/main/resources/story.txt");
                } catch (Exception e) {
                    System.out.println("❌ Failed to load story mode: " + e.getMessage());
                }
                yield null;
            }
        };
    }
}
