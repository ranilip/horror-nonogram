package sk.tuke.kpi.kp.nonogram.service.rest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.nonogram.sentity.Comment;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.CommentService;

import java.util.Arrays;
import java.util.List;


public class CommentServiceRestClient implements CommentService {
    private static final String URL = "http://localhost:8080/api/comment";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) {
        restTemplate.postForEntity(URL, comment, Void.class);
    }

    @Override
    public List<Comment> getComments(String game) {
        var response = restTemplate.getForEntity(URL + "/" + game, Comment[].class);
        return Arrays.asList(response.getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Reset is not supported via REST");
    }
}

