package dev.oscarrojas.issuetracker.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {

    Optional<UserModel> findByUsername(String username);

}
