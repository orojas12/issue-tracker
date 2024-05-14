package dev.oscarrojas.issuetracker.user;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.util.RandomStringGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDaoJpa implements UserDao {

    private UserRepository userRepository;

    public UserDaoJpa(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<UserModel> opt = userRepository.findByUsername(username);
        return opt.map(UserDaoJpa::toEntity);
    }

    @Override
    public List<User> findAll() {
        List<UserModel> models = userRepository.findAll();
        return models.stream().map(UserDaoJpa::toEntity).toList();
    }

    @Override
    public User save(User user) throws DuplicateElementException {
        if (user.getId() == null) {
            return createUser(user);
        } else {
            return updateUser(user);
        }
    }

    @Override
    public void deleteById(String userId) throws NotFoundException {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException("User id '%s' not found.".formatted(userId));
        }
    }

    private User createUser(User user) throws DuplicateElementException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateElementException(
                    "Username '%s' already exists.".formatted(user.getUsername()));
        }

        UserModel model = userRepository.save(new UserModel(
                RandomStringGenerator.getRandomString(10),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateCreated()
        ));

        return toEntity(model);
    }

    private User updateUser(User user) {
        Optional<UserModel> modelOpt = userRepository.findById(user.getId());

        if (modelOpt.isEmpty()) {
            throw new RuntimeException("Could not find user '%s' to update. ".formatted(user.getId()) +
                    "If trying to create new user, user id must be null.");
        }

        UserModel model = modelOpt.get();
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model = userRepository.save(model);
        return toEntity(model);
    }

    private static User toEntity(UserModel model) {
        return new User(
                model.getId(),
                model.getUsername(),
                model.getFirstName(),
                model.getLastName(),
                model.getDateCreated()
        );
    }
}
