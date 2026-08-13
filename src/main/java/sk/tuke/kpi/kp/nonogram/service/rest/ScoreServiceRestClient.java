package sk.tuke.kpi.kp.nonogram.service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.nonogram.sentity.Score;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.ScoreService;

import java.util.Arrays;
import java.util.List;

public class ScoreServiceRestClient implements ScoreService {
    private static final String URL = "http://localhost:3000/api/score";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addScore(Score score) {
        restTemplate.postForEntity(URL, score, Void.class);
    }

    @Override
    public List<Score> getTopScores(String game) {
        var response = restTemplate.getForEntity(URL + "/" + game, Score[].class);
        return Arrays.asList(response.getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Reset is not supported via REST");
    }
}
