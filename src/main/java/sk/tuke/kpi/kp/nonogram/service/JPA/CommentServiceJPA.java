package sk.tuke.kpi.kp.nonogram.service.JPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import sk.tuke.kpi.kp.nonogram.sentity.Comment;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.CommentService;
import sk.tuke.kpi.kp.nonogram.service.exception.CommentException;

import java.time.LocalDate;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) throws CommentException {
        comment.setCommentedOn(LocalDate.now());
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try {
            TypedQuery<Comment> query = entityManager.createQuery(
                    "SELECT c FROM Comment c WHERE c.game = :game ORDER BY c.commentedOn DESC, c.id DESC",
                    Comment.class
            );
            query.setParameter("game", game);
            return query.getResultList();
        } catch (Exception e) {
            throw new CommentException("Failed to load comments", e);
        }
    }

    @Override
    public void reset() throws CommentException {
        try {
            entityManager.createQuery("DELETE FROM Comment").executeUpdate();
        } catch (Exception e) {
            throw new CommentException("Failed to reset comments", e);
        }
    }
}
