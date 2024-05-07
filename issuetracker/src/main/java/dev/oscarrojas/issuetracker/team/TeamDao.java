package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface TeamDao {

    Optional<Team> findById(String id);

    List<Team> findAll();

    Team save(Team team) throws NotFoundException;

    void deleteById(String id) throws NotFoundException;
}
