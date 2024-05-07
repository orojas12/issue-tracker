package dev.oscarrojas.issuetracker.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManager {

    private final UserDao userDao;

    public UserManager(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> getUser(String username) {
        return userDao.findByUsername(username);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userDao.findAll();
        return users.stream()
                .map((user) -> new UserDto(user.getUsername()))
                .toList();
    }
}
