package sk.tuke.kpi.kp.nonogram.service.serviceInterface;

import sk.tuke.kpi.kp.nonogram.sentity.Score;
import sk.tuke.kpi.kp.nonogram.service.exception.ScoreException;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    void reset() throws ScoreException;
}

