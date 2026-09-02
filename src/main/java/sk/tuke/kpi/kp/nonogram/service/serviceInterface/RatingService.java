package sk.tuke.kpi.kp.nonogram.service.serviceInterface;

import sk.tuke.kpi.kp.nonogram.sentity.Rating;
import sk.tuke.kpi.kp.nonogram.service.exception.RatingException;

// Methods as used by consoleUI/PlayerSessionService (the file was empty in the archive).
public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    double getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;
    void reset() throws RatingException;
}
