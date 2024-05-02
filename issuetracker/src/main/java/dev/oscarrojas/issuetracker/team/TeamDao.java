package dev.oscarrojas.issuetracker.team;

import java.util.Optional;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;

public interface TeamDao {

    Optional<Team> findById(String id);
    
    Team update(String id, Team team) throws NotFoundException;

    Team create(Team team);

    void delete(String id) throws NotFoundException;

}
