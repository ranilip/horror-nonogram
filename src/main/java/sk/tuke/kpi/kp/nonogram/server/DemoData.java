package sk.tuke.kpi.kp.nonogram.server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.kpi.kp.nonogram.sentity.Comment;
import sk.tuke.kpi.kp.nonogram.sentity.Rating;
import sk.tuke.kpi.kp.nonogram.sentity.Score;
import sk.tuke.kpi.kp.nonogram.sentity.User;

import java.time.LocalDate;
import java.util.UUID;

// Emulated results so the leaderboard and comments are not empty. Demo accounts have an @demo.invalid
// e-mail and comments carry demo = true (not shown on the site). They get a random password that is
// never printed, so nobody can log in with them. Runs only if no demo account exists yet.
@Component
@ConditionalOnProperty(name = "nonogram.demo-data", havingValue = "true")
public class DemoData implements CommandLineRunner {
    private static final String GAME = "nonogram";

    @PersistenceContext
    private EntityManager em;

    private static final Object[][] PLAYERS = {
            // login, best scores…, days ago of the last game
            {"Keeper", new int[]{24750, 18200, 9500}, 1},
            {"Mermaid", new int[]{21300, 12750}, 2},
            {"Lantern", new int[]{17850, 16400, 4250}, 3},
            {"Gull", new int[]{14500}, 5},
            {"Fog", new int[]{11250, 8750}, 6},
            {"Tide", new int[]{9000, 3500}, 8},
            {"Cistern", new int[]{6750}, 11},
            {"Lens", new int[]{4500, 2250}, 13},
    };

    private static final Object[][] COMMENTS = {
            // player (null = anonymous), stars, text, date (2025)
            {"Keeper", 5, "The face at the window got me. Solved it with shaking hands.", "2025-12-14"},
            {null, 4, "Love the tiles. The timer is cruel, in a good way.", "2025-11-02"},
            {"Mermaid", 5, "She saw me too. When is the full story coming?", "2025-09-21"},
            {"Lantern", 4, "Score Mode with pixel art is addictive. 16x16 is hard.", "2025-08-05"},
            {null, 3, "Nice atmosphere, I wish the clues were bigger on my laptop.", "2025-06-30"},
            {"Gull", 5, "The footsteps before the screamer... I closed the tab and came back.", "2025-04-18"},
            {"Fog", 4, "Lost two lives on the same row. Still fun.", "2025-03-07"},
    };

    @Override
    @Transactional
    public void run(String... args) {
        boolean seeded = !em.createQuery("SELECT u FROM User u WHERE u.email LIKE '%@demo.invalid'", User.class)
                .setMaxResults(1).getResultList().isEmpty();
        if (seeded) return;
        LocalDate today = LocalDate.now();
        for (Object[] p : PLAYERS) {
            String login = (String) p[0];
            em.persist(new User(login, "pbkdf2$1$" + UUID.randomUUID() + "$" + UUID.randomUUID(), login.toLowerCase() + "@demo.invalid"));
            int[] scores = (int[]) p[1];
            int daysAgo = (int) p[2];
            for (int i = 0; i < scores.length; i++)
                em.persist(new Score(login, GAME, scores[i], today.minusDays(daysAgo + i * 2L)));
        }
        for (Object[] c : COMMENTS) {
            String player = (String) c[0];
            int stars = (int) c[1];
            LocalDate day = LocalDate.parse((String) c[3]);
            Comment comment = new Comment(player, GAME, (String) c[2], day);
            comment.setRating(stars);
            comment.setDemo(true);
            em.persist(comment);
            em.persist(new Rating(player, GAME, stars, day));
        }
    }
}
