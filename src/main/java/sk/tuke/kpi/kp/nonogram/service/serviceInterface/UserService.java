package sk.tuke.kpi.kp.nonogram.service.serviceInterface;

import sk.tuke.kpi.kp.nonogram.sentity.User;

public interface UserService {
    void register(User user);
    User login(String login, String password);
    boolean existsLogin(String login);
    boolean existsEmail(String email);
}
