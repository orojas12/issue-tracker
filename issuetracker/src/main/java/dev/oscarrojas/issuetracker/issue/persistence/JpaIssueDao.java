package dev.oscarrojas.issuetracker.issue.persistence;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.issue.IssueDao;
import dev.oscarrojas.issuetracker.issue.dto.IssueDetails;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaIssueDao implements IssueDao {

    private final IssueRepository issueRepository;
    private final JpaIssueFactory issueFactory;

    public JpaIssueDao(IssueRepository issueRepository,
            JpaIssueFactory issueFactory) {
        this.issueRepository = issueRepository;
        this.issueFactory = issueFactory;
    }

    @Override
    public List<IssueDetails> findAllByProjectId(String id) {
        return issueRepository.findAllByProject_Id(id).stream()
                .map(issue -> (IssueDetails) issue)
                .toList();
    }

    @Override
    public Optional<IssueDetails> findById(long id) {
        Optional<JpaIssue> model = issueRepository.findById(id);
        return model.map(issue -> (IssueDetails) issue);
    }

    @Override
    public IssueDetails save(IssueDetails details) {
        JpaIssue issue = issueFactory.fromDetails(details);
        return issueRepository.save(issue);
    }

    @Override
    public void deleteById(long id) throws NotFoundException {
        if (issueRepository.existsById(id)) {
            issueRepository.deleteById(id);
        } else {
            throw new NotFoundException("Issue id '%d' not found.".formatted(id));
        }
    }

}
