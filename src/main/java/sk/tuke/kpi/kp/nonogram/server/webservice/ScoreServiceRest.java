package sk.tuke.kpi.kp.nonogram.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.nonogram.sentity.Score;
import sk.tuke.kpi.kp.nonogram.service.exception.ScoreException;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.ScoreService;

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

    @PostMapping
    public void addScore(@RequestBody Score score, @PathVariable String login) throws ScoreException {
        score.setPlayer(login);
        scoreService.addScore(score);
    }
}
