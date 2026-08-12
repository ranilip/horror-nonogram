package sk.tuke.kpi.kp.nonogram.consoleUI;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.tuke.kpi.kp.nonogram.sentity.*;
import sk.tuke.kpi.kp.nonogram.service.exception.CommentException;
import sk.tuke.kpi.kp.nonogram.service.exception.RatingException;
import sk.tuke.kpi.kp.nonogram.service.exception.ScoreException;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.CommentService;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.RatingService;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.ScoreService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Getter
@Component
@RequiredArgsConstructor
public class PlayerSessionService {

    private final ScoreService scoreService;
    private final CommentService commentService;
    private final RatingService ratingService;

    private Scanner scanner;
    private String gameName;
    private String playerName;

    @PostConstruct
    public void initDefaults() {
        if (this.gameName == null) {
            this.gameName = "nonogram";
        }
    }

    public void setSessionContext(Scanner scanner, String gameName) {
        this.scanner = scanner;
        this.gameName = gameName;
    }

    public void initPlayer() {
        if (scanner == null) {
            this.scanner = new Scanner(System.in); // fallback
        }
        System.out.print("\nEnter your name: ");
        this.playerName = scanner.nextLine();
    }

    public void showPreviousComments() {
        System.out.println("\nComments from other players:");
        try {
            List<Comment> comments = commentService.getComments(gameName);
            for (Comment comment : comments) {
                int ratingValue = ratingService.getRating(gameName, comment.getPlayer());
                String stars = "★".repeat(ratingValue) + "☆".repeat(5 - ratingValue);
                System.out.println("-".repeat(165));
                System.out.printf(
                        "%s | %s | %s | %s%n",
                        comment.getPlayer(),
                        comment.getCommentedOn(),
                        stars,
                        comment.getComment()
                );
                System.out.println("-".repeat(165));
            }
            System.out.println("\nPress Enter for start.");
            scanner.nextLine();

        } catch (Exception e) {
            System.out.println("⚠️ Error loading comments: " + e.getMessage());
        }
    }

    public void requestRating() {
        System.out.print("\nRate the game (1–5): ");
        int rate = Integer.parseInt(scanner.nextLine());
        Rating rating = new Rating(playerName, gameName, rate, LocalDate.now());
        try {
            ratingService.setRating(rating);
        } catch (Exception e) {
            System.out.println("⚠️ Could not save rating.");
        }
    }

    public void requestComment() {
        System.out.print("\nLeave a comment: ");
        String text = scanner.nextLine();
        Comment comment = new Comment(playerName, gameName, text, LocalDate.now());
        try {
            commentService.addComment(comment);
        } catch (Exception e) {
            System.out.println("⚠️ Could not save comment.");
        }
    }

    public void saveScore(int score) {
        try {
            scoreService.addScore(new Score(playerName, gameName, score, LocalDate.now()));
        } catch (Exception e) {
            System.out.println("⚠️ Could not save score.");
        }
    }

    public List<Score> getTopScores() {
        try {
            List<Score> scores = scoreService.getTopScores(gameName);

            System.out.println("\nTop Scores for game: " + gameName);
            System.out.println("-".repeat(165));
            for (Score score : scores) {
                System.out.printf("%-10s | %5d points | %s%n",
                        score.getPlayer(),
                        score.getPoints(),
                        score.getPlayedOn());
            }
            System.out.println("-".repeat(165));

            return scores;
        } catch (ScoreException e) {
            System.err.println("Failed to load top scores: " + e.getMessage());
            return List.of();
        }
    }


    public void resetScores() {
        try {
            scoreService.reset();
            System.out.println("All scores have been reset.");
        } catch (ScoreException e) {
            System.err.println("Failed to reset scores: " + e.getMessage());
        }
    }

    public double getAverageRating() {
        try {
            double avg = ratingService.getAverageRating(gameName);
            String stars = "★".repeat((int)avg) + "☆".repeat(5 - (int)avg);
            System.out.println("\nRating hry: " + stars);
            return avg;
        } catch (RatingException e) {
            System.err.println("Nepodarilo sa získať priemerné hodnotenie: " + e.getMessage());
            return 0;
        }
    }


    public void resetRatings() {
        try {
            ratingService.reset();
            System.out.println("All ratings have been reset.");
        } catch (RatingException e) {
            System.err.println("Failed to reset ratings: " + e.getMessage());
        }
    }

    public void resetComments() {
        try {
            commentService.reset();
            System.out.println("All comments have been reset.");
        } catch (CommentException e) {
            System.err.println("Failed to reset comments: " + e.getMessage());
        }
    }
}
