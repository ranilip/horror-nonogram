package sk.tuke.kpi.kp.nonogram.service.JPA;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.kpi.kp.nonogram.sentity.User;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.UserService;

@Service
@Transactional
public class UserServiceJPA implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void register(User user) {
        entityManager.persist(user);
    }

    @Override
    public User login(String login, String password) {
        return entityManager.createNamedQuery("User.findByLogin", User.class)
                .setParameter("login", login)
                .getResultList()
                .stream()
                .filter(u -> u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean existsLogin(String login) {
        return !entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                .setParameter("login", login).getResultList().isEmpty();
    }

    @Override
    public boolean existsEmail(String email) {
        return !entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email).getResultList().isEmpty();
    }
}
