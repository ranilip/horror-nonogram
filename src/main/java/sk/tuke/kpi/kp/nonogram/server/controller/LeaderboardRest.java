package sk.tuke.kpi.kp.nonogram.server.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Leaderboard: only registered players (present in the users table), each with their best score.
@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardRest {

    public record Entry(int rank, String player, int bestScore, long games, LocalDate lastPlayed) {}

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/{game}")
    @Transactional(readOnly = true)
    public List<Entry> leaderboard(@PathVariable String game, @RequestParam(defaultValue = "10") int limit) {
        List<Object[]> rows = entityManager.createQuery(
                        "SELECT s.player, MAX(s.points), COUNT(s), MAX(s.playedOn) FROM Score s " +
                        "WHERE s.game = :game AND s.player IN (SELECT u.login FROM User u) " +
                        "GROUP BY s.player ORDER BY MAX(s.points) DESC, MAX(s.playedOn) ASC", Object[].class)
                .setParameter("game", game)
                .setMaxResults(Math.max(1, Math.min(limit, 50)))
                .getResultList();
        int[] rank = {0};
        return rows.stream()
                .map(r -> new Entry(++rank[0], (String) r[0], ((Number) r[1]).intValue(), ((Number) r[2]).longValue(), (LocalDate) r[3]))
                .toList();
    }
}
