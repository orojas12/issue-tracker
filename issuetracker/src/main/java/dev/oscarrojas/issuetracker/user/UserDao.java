package dev.oscarrojas.issuetracker.user;

import java.util.Optional;

public interface UserDao {

    Optional<User> findByUsername(String username);

}