package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.issue.dto.IssueDetails;

import java.util.List;
import java.util.Optional;

public interface IssueDao {

    List<IssueDetails> findAllByProjectId(String projectId);

    Optional<IssueDetails> findById(long id);

    IssueDetails save(IssueDetails issue);

    void deleteById(long id) throws NotFoundException;
}
