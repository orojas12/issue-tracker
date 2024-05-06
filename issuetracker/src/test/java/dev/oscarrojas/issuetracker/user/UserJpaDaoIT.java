package dev.oscarrojas.issuetracker.user;

import dev.oscarrojas.issuetracker.IntegrationTestConfig;
import dev.oscarrojas.issuetracker.IssueTracker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {IssueTracker.class, IntegrationTestConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserDaoJpa.class)
public class UserJpaDaoIT {

    @Autowired
    TestEntityManager em;

    @Autowired
    UserDao userDao;

    private UserModel withUsername(String username) {
        return new UserModel(username, "password", Instant.now());
    }

    @Test
    void findByUsername_Username_returnsUser() {
        UserModel userModel = em.persist(withUsername("user1"));
        Optional<User> userOpt = userDao.findByUsername(userModel.getUsername());
        assertTrue(userOpt.isPresent());
        User user = userOpt.get();
        assertEquals(userModel.getUsername(), user.getUsername());
        assertEquals(userModel.getDateCreated(), user.getDateCreated());
    }

    @Test
    void findByUsername_Username_returnsEmptyIfNotFound() {
        Optional<User> userOpt = userDao.findByUsername("user1");
        assertTrue(userOpt.isEmpty());
    }

    @Test
    void findAll_returnsAllUsers() {
        UserModel model1 = em.persist(withUsername("user1"));
        UserModel model2 = em.persist(withUsername("user2"));
        List<User> users = userDao.findAll();
        boolean hasUser1 = false;
        boolean hasUser2 = false;
        for (User user : users) {
            if (user.getUsername().equals(model1.getUsername())) {
                hasUser1 = true;
            } else if (user.getUsername().equals(model2.getUsername())) {
                hasUser2 = true;
            }
        }
        assertTrue(hasUser1 && hasUser2);
    }
}
