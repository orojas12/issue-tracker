package dev.oscarrojas.issuetracker.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

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
}
