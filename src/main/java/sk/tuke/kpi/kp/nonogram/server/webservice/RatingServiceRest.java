package sk.tuke.kpi.kp.nonogram.server.webservice;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.nonogram.sentity.Rating;
import sk.tuke.kpi.kp.nonogram.server.controller.UserServiceRest;
import sk.tuke.kpi.kp.nonogram.service.exception.RatingException;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.RatingService;

import java.time.LocalDate;

// Endpoints used by RatingServiceRestClient (console client) and by the web Comments page.
@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/{game}")
    public double getAverageRating(@PathVariable String game) throws RatingException {
        return ratingService.getAverageRating(game);
    }

    @GetMapping("/{game}/{player}")
    public int getRating(@PathVariable String game, @PathVariable String player) throws RatingException {
        return ratingService.getRating(game, player);
    }

    @PostMapping
    public ResponseEntity<String> setRating(@RequestBody Rating rating, HttpSession session) throws RatingException {
        if (rating.getRating() < 1 || rating.getRating() > 5) return ResponseEntity.badRequest().body("Rating must be 1–5 stars");
        Object login = session.getAttribute(UserServiceRest.SESSION_LOGIN);
        rating.setId(null);
        rating.setPlayer(login == null ? rating.getPlayer() : login.toString());
        if (rating.getGame() == null || rating.getGame().isBlank()) rating.setGame("nonogram");
        rating.setRatedOn(LocalDate.now());
        ratingService.setRating(rating);
        return ResponseEntity.ok("Rating saved");
    }
}
