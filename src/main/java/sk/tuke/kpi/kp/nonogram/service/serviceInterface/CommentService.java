package sk.tuke.kpi.kp.nonogram.service.serviceInterface;

import sk.tuke.kpi.kp.nonogram.sentity.Comment;
import sk.tuke.kpi.kp.nonogram.service.exception.CommentException;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
    void reset() throws CommentException;
}
