package sk.tuke.kpi.kp.nonogram.service.JDBC;

import sk.tuke.kpi.kp.nonogram.sentity.Score;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.ScoreService;
import sk.tuke.kpi.kp.nonogram.service.exception.ScoreException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceJDBC implements ScoreService {

    private static final String URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public ScoreServiceJDBC() throws ScoreException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ScoreException("Cannot load JDBC driver", e);
        }
    }

    @Override
    public void addScore(Score score) throws ScoreException {
        String sql = "INSERT INTO score (player, game, points, playedOn) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, score.getPlayer());
            statement.setString(2, score.getGame());
            statement.setInt(3, score.getPoints());
            statement.setDate(4, Date.valueOf(score.getPlayedOn()));
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ScoreException("Error saving score", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        String sql = "SELECT player, game, points, playedOn FROM score WHERE game = ? ORDER BY points DESC LIMIT 10";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, game);

            try (ResultSet rs = statement.executeQuery()) {
                List<Score> scores = new ArrayList<>();

                while (rs.next()) {
                    scores.add(new Score(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getInt(3),
                            rs.getDate(4).toLocalDate()
                    ));
                }

                return scores;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ScoreException("Error retrieving top scores", e);
        }
    }

    @Override
    public void reset() throws ScoreException {
        String sql = "DELETE FROM score";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ScoreException("Error resetting scores", e);
        }
    }
}
