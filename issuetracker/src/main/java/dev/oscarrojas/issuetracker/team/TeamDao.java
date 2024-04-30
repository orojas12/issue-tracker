package dev.oscarrojas.issuetracker.team;

import java.util.Optional;

public interface TeamDao {

    Optional<Team> findById(String id);
    
    Team update(Team team);

    Team create(Team team);

    void delete(String id);

}
