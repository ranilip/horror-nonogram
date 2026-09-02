package sk.tuke.kpi.kp.nonogram.service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.nonogram.sentity.Rating;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.RatingService;

// Same pattern as CommentServiceRestClient.
public class RatingServiceRestClient implements RatingService {
    private static final String URL = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(URL, rating, Void.class);
    }

    @Override
    public double getAverageRating(String game) {
        Double average = restTemplate.getForObject(URL + "/" + game, Double.class);
        return average == null ? 0 : average;
    }

    @Override
    public int getRating(String game, String player) {
        Integer rating = restTemplate.getForObject(URL + "/" + game + "/" + player, Integer.class);
        return rating == null ? 0 : rating;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Reset is not supported via REST");
    }
}
