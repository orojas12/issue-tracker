package dev.oscarrojas.issuetracker.user;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static dev.oscarrojas.issuetracker.TestUtils.userWithUsername;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserManagerTest {

    @Mock
    TestUserDao userDao;

    @Test
    void getUser_Username_returnsUserDetails() {
        User user = userWithUsername("user1");
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserManager userManager = new UserManager(userDao);

        Optional<UserDto> opt = userManager.getUser(user.getUsername());

        assertTrue(opt.isPresent());
        UserDto result = opt.get();
        assertEquals(user.getId(), result.id());
        assertEquals(user.getUsername(), result.username());
        assertEquals(user.getFirstName(), result.firstName());
        assertEquals(user.getLastName(), result.lastName());
        assertEquals(user.getDateCreated(), result.dateCreated());
    }

    @Test
    void getUser_Username_returnsEmptyIfNotFound() {
        User user = userWithUsername("user1");
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        UserManager userManager = new UserManager(userDao);

        Optional<UserDto> opt = userManager.getUser(user.getUsername());

        assertTrue(opt.isEmpty());
    }

    @Test
    void getAllUsers_returnsAllUsers() {
        List<User> users = Arrays.asList(userWithUsername("user1"), userWithUsername("user2"));
        when(userDao.findAll()).thenReturn(users);
        UserManager userManager = new UserManager(userDao);

        List<UserDto> results = userManager.getAllUsers();

        assertEquals(users.size(), results.size());
        boolean hasUser1 = false;
        boolean hasUser2 = false;
        for (UserDto result : results) {
            if (result.username().equals("user1")) {
                hasUser1 = true;
            } else if (result.username().equals("user2")) {
                hasUser2 = true;
            }
        }
        assertTrue(hasUser1 && hasUser2);
    }

    @Test
    void createUser_savesUserWithNullId() throws DuplicateElementException {
        // a null user id is necessary to tell UserDao to create a new user
        when(userDao.save(argThat(arg -> arg.getId() == null))).thenAnswer(i -> i.getArguments()[0]);
        UserManager userManager = new UserManager(userDao);
        var request = new CreateUserRequest("john1", "John", "Wick");

        userManager.createUser(request);
    }

    @Test
    void createUser_returnsCreatedUserData() throws DuplicateElementException {
        when(userDao.save(argThat(arg -> arg.getId() == null))).thenAnswer(i -> {
            User user = (User) i.getArguments()[0];
            user.setId("id");
            user.setDateCreated(Instant.now());
            return user;
        });
        UserManager userManager = new UserManager(userDao);
        var request = new CreateUserRequest("john1", "John", "Wick");

        UserDto dto = userManager.createUser(request);

        assertNotNull(dto.id());
        assertEquals(request.username(), dto.username());
        assertEquals(request.firstName(), dto.firstName());
        assertEquals(request.lastName(), dto.lastName());
        assertNotNull(dto.id());
    }

    static class TestUserDao implements UserDao {

        @Override
        public Optional<User> findByUsername(String username) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public List<User> findAll() {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public User save(User user) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public void deleteById(String userId) {
            throw new RuntimeException("Unimplemented method");
        }
    }
}
