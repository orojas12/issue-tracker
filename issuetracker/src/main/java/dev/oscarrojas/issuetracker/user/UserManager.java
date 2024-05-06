package dev.oscarrojas.issuetracker.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManager {

    private UserDao userDao;

    public UserManager(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> getUser(String username) {
        Optional<User> user = userDao.findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(user.get());
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userDao.findAll();
        return users.stream()
                .map((user) -> new UserDto(user.getUsername()))
                .toList();
    }
}
