package dev.oscarrojas.issuetracker.user;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManager {

    private final UserDao userDao;

    public UserManager(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<UserDto> getUser(String username) {
        Optional<User> userOpt = userDao.findByUsername(username);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        } else {
            User user = userOpt.get();
            return Optional.of(new UserDto(
                    user.getId(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getDateCreated()
            ));
        }
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userDao.findAll();
        return users.stream()
                .map((user) ->
                        new UserDto(
                                user.getId(),
                                user.getUsername(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getDateCreated()))
                .toList();
    }

    public UserDto createUser(CreateUserRequest request) throws DuplicateElementException {
        User user = new UserBuilder().username(request.username())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        user = userDao.save(user);

        return new UserDto(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateCreated());
    }
}
