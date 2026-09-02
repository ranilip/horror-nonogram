package sk.tuke.kpi.kp.nonogram.server.webservice;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.nonogram.sentity.Comment;
import sk.tuke.kpi.kp.nonogram.sentity.Rating;
import sk.tuke.kpi.kp.nonogram.server.controller.UserServiceRest;
import sk.tuke.kpi.kp.nonogram.service.exception.CommentException;
import sk.tuke.kpi.kp.nonogram.service.exception.RatingException;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.CommentService;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.RatingService;

import java.time.LocalDate;
import java.util.List;

// Comments with stars. Logged-in players post under their login, everyone else anonymously (player = null).
@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

    public record NewComment(String game, String comment, Integer rating) {}

    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    @GetMapping("/{game}")
    public List<Comment> getComments(@PathVariable String game) throws CommentException {
        return commentService.getComments(game);
    }

    @PostMapping
    public ResponseEntity<String> addComment(@RequestBody NewComment body, HttpSession session) throws CommentException, RatingException {
        String text = body.comment() == null ? "" : body.comment().trim();
        if (text.isEmpty() || text.length() > 255) return ResponseEntity.badRequest().body("Comment must be 1–255 characters");
        Integer stars = body.rating();
        if (stars != null && (stars < 1 || stars > 5)) return ResponseEntity.badRequest().body("Rating must be 1–5 stars");
        String game = body.game() == null || body.game().isBlank() ? "nonogram" : body.game();
        Object login = session.getAttribute(UserServiceRest.SESSION_LOGIN);
        String player = login == null ? null : login.toString();

        Comment comment = new Comment(player, game, text, LocalDate.now());
        comment.setRating(stars);
        commentService.addComment(comment);
        // Also recorded in the original rating table, so the game's average rating includes it.
        if (stars != null) ratingService.setRating(new Rating(player, game, stars, LocalDate.now()));
        return ResponseEntity.ok(player == null ? "Comment posted anonymously" : "Comment posted");
    }
}
