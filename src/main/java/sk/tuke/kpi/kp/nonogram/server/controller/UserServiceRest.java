package sk.tuke.kpi.kp.nonogram.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.nonogram.sentity.User;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.UserService;

@RestController
@RequestMapping("/api/user")
public class UserServiceRest {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (!user.getLogin().matches("^[a-zA-Z0-9_]+$"))
            return "Invalid login format";

        if (userService.existsLogin(user.getLogin()))
            return "Login already exists";

        if (userService.existsEmail(user.getEmail()))
            return "Email already used";

        userService.register(user);
        return "Registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User found = userService.login(user.getLogin(), user.getPassword());
        return (found != null)
                ? "OK" : "Invalid credentials";
    }
}
