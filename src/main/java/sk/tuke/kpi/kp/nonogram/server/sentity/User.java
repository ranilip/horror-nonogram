package sk.tuke.kpi.kp.nonogram.sentity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.login = :login")
public class User {

    @Id
    @GeneratedValue
    private int id;

    // The React sign-up form sends "player", the login form sends "login".
    @JsonAlias("player")
    @Column(nullable = false, unique = true)
    private String login;

    // Stored as a PBKDF2 hash; never sent back in JSON.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    public User() {}

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}

