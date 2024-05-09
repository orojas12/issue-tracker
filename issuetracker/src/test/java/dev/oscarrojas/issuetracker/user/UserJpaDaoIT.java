package dev.oscarrojas.issuetracker.user;

import dev.oscarrojas.issuetracker.IntegrationTestConfig;
import dev.oscarrojas.issuetracker.IssueTracker;
import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static dev.oscarrojas.issuetracker.TestUtils.userModelWithUsername;
import static dev.oscarrojas.issuetracker.TestUtils.userWithUsername;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {IssueTracker.class, IntegrationTestConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(UserDaoJpa.class)
public class UserJpaDaoIT {

    @Autowired
    TestEntityManager em;

    @Autowired
    UserDao userDao;

    @Test
    void findByUsername_Username_returnsUser() {
        UserModel userModel = em.persist(userModelWithUsername("user1"));
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
        UserModel model1 = em.persist(userModelWithUsername("user1"));
        UserModel model2 = em.persist(userModelWithUsername("user2"));
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

    @Test
    void save_returnsSavedUser() throws DuplicateElementException, NotFoundException {
        User user = new UserBuilder().username("user1").firstName("John").lastName("Smith").build();

        User result = userDao.save(user);

        assertNotNull(result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(LocalDate.now().getDayOfYear(),
                LocalDate.ofInstant(result.getDateCreated(), ZoneId.systemDefault()).getDayOfYear());
    }

    @Test
    void save_persistsNewUserToDatabase() throws DuplicateElementException, NotFoundException {
        User user = userWithUsername("john1");
        // a user with null id means it does not exist and should be created in db
        user.setId(null);

        User result = userDao.save(user);

        UserModel model = em.find(UserModel.class, result.getId());
        assertNotNull(model);
        assertNotNull(model.getId());
        assertEquals(user.getUsername(), model.getUsername());
        assertEquals(user.getFirstName(), model.getFirstName());
        assertEquals(user.getLastName(), model.getLastName());
        assertEquals(user.getDateCreated(), model.getDateCreated());
    }

    @Test
    void save_throwsDuplicateElementIfNewUserAlreadyExists() {
        em.persistAndFlush(userModelWithUsername("john1"));
        User user = userWithUsername("john1");
        user.setId(null);

        assertThrows(DuplicateElementException.class, () -> userDao.save(user));
    }

    @Test
    void save_updatesExistingUserInDatabase() throws DuplicateElementException, NotFoundException {
        UserModel userModel = em.persistFlushFind(userModelWithUsername("john1"));
        User user = new User(userModel.getId(),
                userModel.getUsername(),
                userModel.getFirstName(),
                userModel.getLastName(),
                userModel.getDateCreated());
        user.setFirstName("Bob");
        user.setLastName("Smith");

        userDao.save(user);

        UserModel result = em.find(UserModel.class, user.getId());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getDateCreated(), result.getDateCreated());
    }

    @Test
    void save_throwsRuntimeExceptionIfUpdatingUserThatDoesNotExist() throws DuplicateElementException, NotFoundException {
        User user = userWithUsername("john1");

        assertThrows(RuntimeException.class, () -> userDao.save(user));
    }
}
