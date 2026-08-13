package sk.tuke.kpi.kp.nonogram.sentity;

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

    @Column(nullable = false, unique = true)
    private String login;

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

