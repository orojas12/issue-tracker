package dev.oscarrojas.issuetracker.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

    Optional<UserModel> findByUsername(String username);

    @Query("select count(*) from UserModel u where u.username in ?1")
    int countByUsername(Collection<String> usernames);
}
