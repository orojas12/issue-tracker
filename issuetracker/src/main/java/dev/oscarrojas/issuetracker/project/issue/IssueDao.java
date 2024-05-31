package dev.oscarrojas.issuetracker.project.issue;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface IssueDao {

    List<Issue> findAll();

    Optional<Issue> findById(long id);

    Issue save(Issue issue);

    void deleteById(long id) throws NotFoundException;
}
