package dev.oscarrojas.issuetracker.issue;

import java.util.List;
import java.util.Optional;

public interface IssueDao {

    List<Issue> findAll();

    Optional<Issue> findById(long id);

    Issue save(Issue issue);
}
