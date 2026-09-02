package sk.tuke.kpi.kp.nonogram.server.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.nonogram.sentity.User;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserServiceRest {
    // Session attribute holding the logged-in player's login.
    public static final String SESSION_LOGIN = "login";

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (user.getLogin() == null || !user.getLogin().matches("^[a-zA-Z0-9_]+$"))
            return "Invalid login format";
        if (user.getPassword() == null || user.getPassword().length() < 8)
            return "Password must be at least 8 characters";
        if (user.getEmail() == null || !user.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            return "Invalid email format";
        if (userService.existsLogin(user.getLogin()))
            return "Login already exists";
        if (userService.existsEmail(user.getEmail()))
            return "Email already used";
        userService.register(user);
        return "Registered successfully";
    }

    // Original text contract: "OK" or "Invalid credentials". The login is now also kept in the session,
    // so scores and comments are attributed by the server, not by whatever name the browser sends.
    @PostMapping("/login")
    public String login(@RequestBody User user, HttpSession session) {
        User found = userService.login(user.getLogin(), user.getPassword());
        if (found == null) return "Invalid credentials";
        session.setAttribute(SESSION_LOGIN, found.getLogin());
        return "OK";
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {
        Object login = session.getAttribute(SESSION_LOGIN);
        return login == null ? Map.of("loggedIn", false) : Map.of("loggedIn", true, "login", login);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "OK";
    }
}
