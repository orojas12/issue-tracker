package dev.oscarrojas.issuetracker.user;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findByUsername(String username);

    List<User> findAll();

    User save(User user) throws DuplicateElementException;
}
