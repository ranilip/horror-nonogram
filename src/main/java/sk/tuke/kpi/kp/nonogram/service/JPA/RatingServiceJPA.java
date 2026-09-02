package sk.tuke.kpi.kp.nonogram.service.JPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.kpi.kp.nonogram.sentity.Rating;
import sk.tuke.kpi.kp.nonogram.service.exception.RatingException;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.RatingService;

import java.time.LocalDate;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    // A registered player has one rating per game (a new one replaces the old one);
    // anonymous ratings (player == null) are each kept.
    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            if (rating.getRatedOn() == null) rating.setRatedOn(LocalDate.now());
            if (rating.getPlayer() != null) {
                List<Rating> existing = entityManager.createQuery(
                                "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player", Rating.class)
                        .setParameter("game", rating.getGame())
                        .setParameter("player", rating.getPlayer())
                        .getResultList();
                if (!existing.isEmpty()) {
                    Rating current = existing.get(0);
                    current.setRating(rating.getRating());
                    current.setRatedOn(rating.getRatedOn());
                    return;
                }
            }
            entityManager.persist(rating);
        } catch (Exception e) {
            throw new RatingException("Failed to save rating", e);
        }
    }

    @Override
    public double getAverageRating(String game) throws RatingException {
        try {
            Double average = entityManager.createQuery(
                            "SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game", Double.class)
                    .setParameter("game", game)
                    .getSingleResult();
            return average == null ? 0 : average;
        } catch (Exception e) {
            throw new RatingException("Failed to load average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            List<Rating> ratings = entityManager.createQuery(
                            "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player ORDER BY r.id DESC", Rating.class)
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .setMaxResults(1)
                    .getResultList();
            return ratings.isEmpty() ? 0 : ratings.get(0).getRating();
        } catch (Exception e) {
            throw new RatingException("Failed to load rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try {
            entityManager.createQuery("DELETE FROM Rating").executeUpdate();
        } catch (Exception e) {
            throw new RatingException("Failed to reset ratings", e);
        }
    }
}
