package sk.tuke.kpi.kp.nonogram.service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.nonogram.sentity.User;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.UserService;

public class UserServiceRestClient implements UserService {

    private static final String URL = "http://localhost:8080/api/user";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void register(User user) {
        String response = restTemplate.postForObject(URL + "/register", user, String.class);
        System.out.println("Registration response: " + response);
    }

    @Override
    public User login(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        String response = restTemplate.postForObject(URL + "/login", user, String.class);
        if ("OK".equals(response)) {
            return user; // Повертаємо користувача якщо все гаразд
        }
        System.out.println("Login response: " + response);
        return null;
    }

    @Override
    public boolean existsLogin(String login) {
        throw new UnsupportedOperationException("Not supported via REST");
    }

    @Override
    public boolean existsEmail(String email) {
        throw new UnsupportedOperationException("Not supported via REST");
    }
}

