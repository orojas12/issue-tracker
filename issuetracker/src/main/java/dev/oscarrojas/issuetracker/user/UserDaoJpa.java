package dev.oscarrojas.issuetracker.user;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class UserDaoJpa implements UserDao {

    private UserRepository repository;

    public UserDaoJpa(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<UserModel> opt = repository.findByUsername(username);
        if (opt.isPresent()) {
            UserModel entity = opt.get();
            User user = new User(
                entity.getUsername(), 
                entity.getRoles().stream().map((role) -> role.getId()).collect(Collectors.toSet()),
                entity.getDateCreated()
            );
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

}
