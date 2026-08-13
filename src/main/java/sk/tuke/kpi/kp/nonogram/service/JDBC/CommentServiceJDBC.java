package sk.tuke.kpi.kp.nonogram.service.JDBC;

import sk.tuke.kpi.kp.nonogram.sentity.Comment;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.CommentService;
import sk.tuke.kpi.kp.nonogram.service.exception.CommentException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {

    private static final String URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public CommentServiceJDBC() throws CommentException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new CommentException("Cannot load JDBC driver", e);
        }
    }

    @Override
    public void addComment(Comment comment) throws CommentException {
        String sql = "INSERT INTO comment (player, game, comment, commentedOn) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, comment.getPlayer());
            statement.setString(2, comment.getGame());
            statement.setString(3, comment.getComment());
            statement.setDate(4, Date.valueOf(comment.getCommentedOn()));
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new CommentException("Error saving comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        String sql = "SELECT player, game, comment, commentedOn FROM comment WHERE game = ? ORDER BY commentedOn DESC";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, game);

            try (ResultSet rs = statement.executeQuery()) {
                List<Comment> comments = new ArrayList<>();

                while (rs.next()) {
                    comments.add(new Comment(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getDate(4).toLocalDate()
                    ));
                }

                return comments;
            }

        } catch (SQLException e) {
            throw new CommentException("Error loading comments", e);
        }
    }

    @Override
    public void reset() throws CommentException {
        String sql = "DELETE FROM comment";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            throw new CommentException("Error resetting comments", e);
        }
    }
}

