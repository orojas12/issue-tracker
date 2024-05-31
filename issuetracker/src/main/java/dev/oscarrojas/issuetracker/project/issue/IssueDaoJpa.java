package dev.oscarrojas.issuetracker.project.issue;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class IssueDaoJpa implements IssueDao {

    private final IssueRepository issueRepository;

    public IssueDaoJpa(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public List<Issue> findAll() {
        List<IssueModel> issues = issueRepository.findAll();
        return issues.stream().map(IssueDaoJpa::toEntity).toList();
    }

    @Override
    public Optional<Issue> findById(long id) {
        Optional<IssueModel> model = issueRepository.findById(id);
        return model.map(IssueDaoJpa::toEntity);
    }

    @Override
    public Issue save(Issue issue) {
        IssueModel model;
        if (issue.getId() != null) {
            Optional<IssueModel> opt = issueRepository.findById(issue.getId());
            model = opt.orElseThrow(() -> new RuntimeException(
                    String.format(
                            "Could not find team id '%s'. If this is a new team" +
                                    " please set team id to null before saving.",
                            issue.getId())));
            model.setTitle(issue.getTitle());
            model.setDescription(issue.getDescription());
            if (issue.getDueDate() != null) {
                model.setDueDate(issue.getDueDate().toLocalDateTime());
                model.setDueDateTimeZone(issue.getDueDate().getZone());
            } else {
                model.setDueDate(null);
                model.setDueDateTimeZone(null);
            }
            model.setClosed(issue.isClosed());
        } else {
            model = new IssueModel(null,
                    issue.getTitle(),
                    issue.getDescription(),
                    issue.getCreatedAt(),
                    issue.getDueDate() != null ? issue.getDueDate().toLocalDateTime() : null,
                    issue.getDueDate() != null ? issue.getDueDate().getZone() : null,
                    false);
        }
        model = issueRepository.save(model);
        return toEntity(model);
    }

    @Override
    public void deleteById(long id) throws NotFoundException {
        if (issueRepository.existsById(id)) {
            issueRepository.deleteById(id);
        } else {
            throw new NotFoundException("Issue id '%d' not found.".formatted(id));
        }
    }

    private static Issue toEntity(IssueModel model) {
        var issue = new Issue();
        issue.setId(model.getId());
        issue.setTitle(model.getTitle());
        issue.setDescription(model.getDescription());
        issue.setCreatedAt(model.getCreatedAt());
        issue.setClosed(model.isClosed());
        if (model.getDueDate() != null && model.getDueDateTimeZone() != null) {
            issue.setDueDate(ZonedDateTime.of(model.getDueDate(), model.getDueDateTimeZone()));
        } else {
            issue.setDueDate(null);
        }
        return issue;
    }
}
