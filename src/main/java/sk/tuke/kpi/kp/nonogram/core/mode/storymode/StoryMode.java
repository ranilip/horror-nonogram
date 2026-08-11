package sk.tuke.kpi.kp.nonogram.core.mode.storymode;

import lombok.Getter;
import sk.tuke.kpi.kp.nonogram.core.Generate;
import sk.tuke.kpi.kp.nonogram.core.mode.Game;

@Getter
public class StoryMode extends Game {
    private final Long timeLimit; // null = no limit
    private Integer lives;        // null = unlimited

    public StoryMode(Generate.Nonogram nonogram, Integer lives, Integer timeSeconds) {
        super(nonogram);
        this.lives = lives;
        this.timeLimit = (timeSeconds != null) ? System.currentTimeMillis() + (timeSeconds * 1000L) : null;
    }

    @Override
    protected void applyPenalty() {
        lives--;
        if (lives >= 1) {
            System.out.println("\n🔪 You lost a life! Remaining: " + "❤️".repeat(lives));
        }
    }

    @Override
    protected boolean checkFailCondition() {
        boolean timeUp = timeLimit != null && System.currentTimeMillis() > timeLimit;
        boolean noLives = lives != null && lives <= 0;
        return timeUp || noLives;
    }

    public long getRemainingTimeSeconds() {
        if (timeLimit == null) return -1;
        return (timeLimit - System.currentTimeMillis()) / 1000L;
    }
}
