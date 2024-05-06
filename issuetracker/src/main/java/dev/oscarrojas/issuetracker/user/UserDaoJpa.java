package dev.oscarrojas.issuetracker.user;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
                entity.getDateCreated()
            );
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        List<UserModel> models = repository.findAll();
        return (List<User>) UserEntityModelMapper.getEntities(models);
    }

}
