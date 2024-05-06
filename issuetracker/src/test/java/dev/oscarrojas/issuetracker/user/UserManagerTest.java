package dev.oscarrojas.issuetracker.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static dev.oscarrojas.issuetracker.TestUtils.userWithUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserManagerTest {

    @Mock
    TestUserDao userDao;

    @Test
    void getUser_Username_returnsUser() {
        User user = userWithUsername("user1");
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserManager userManager = new UserManager(userDao);
        Optional<User> opt = userManager.getUser(user.getUsername());

        assertTrue(opt.isPresent());
        User result = opt.get();
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void getUser_Username_returnsEmptyIfNotFound() {
        User user = userWithUsername("user1");
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        UserManager userManager = new UserManager(userDao);
        Optional<User> opt = userManager.getUser(user.getUsername());

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

    static class TestUserDao implements UserDao {

        @Override
        public Optional<User> findByUsername(String username) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public List<User> findAll() {
            throw new RuntimeException("Unimplemented method");
        }
    }
}
