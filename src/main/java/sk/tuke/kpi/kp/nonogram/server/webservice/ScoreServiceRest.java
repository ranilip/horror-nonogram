package sk.tuke.kpi.kp.nonogram.server.webservice;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.nonogram.sentity.Score;
import sk.tuke.kpi.kp.nonogram.server.controller.UserServiceRest;
import sk.tuke.kpi.kp.nonogram.service.exception.ScoreException;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.ScoreService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) throws ScoreException {
        return scoreService.getTopScores(game);
    }

    // The archived version read the player from a @PathVariable that is not in the URL (it could not work).
    // The player now comes from the login session; guests get 401.
    @PostMapping
    public ResponseEntity<String> addScore(@RequestBody Score score, HttpSession session) throws ScoreException {
        Object login = session.getAttribute(UserServiceRest.SESSION_LOGIN);
        if (login == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Log in to save your score");
        if (score.getPoints() < 0 || score.getPoints() > 10_000_000) return ResponseEntity.badRequest().body("Invalid score");
        score.setId(null);
        score.setPlayer(login.toString());
        if (score.getGame() == null || score.getGame().isBlank()) score.setGame("nonogram");
        score.setPlayedOn(LocalDate.now());
        scoreService.addScore(score);
        return ResponseEntity.ok("Score saved");
    }
}
