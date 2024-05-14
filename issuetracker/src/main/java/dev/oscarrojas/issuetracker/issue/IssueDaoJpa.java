package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

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
        IssueModel model = null;
        if (issue.getId() != null) {
            Optional<IssueModel> opt = issueRepository.findById(issue.getId());
            model = opt.orElseThrow(() -> new RuntimeException(
                    String.format(
                            "Could not find team id '%s'. If this is a new team" +
                                    " please set team id to null before saving.",
                            issue.getId()
                    )
            ));
            model.setTitle(issue.getTitle());
            model.setDescription(issue.getDescription());
            model.setDueDate(issue.getDueDate());
            model.setClosed(issue.isClosed());
        } else {
            model = new IssueModel(null,
                    issue.getTitle(),
                    issue.getDescription(),
                    issue.getCreatedAt(),
                    issue.getDueDate(),
                    false);
        }
        model = issueRepository.save(model);
        return toEntity(model);
    }

    public void deleteById(long id) throws NotFoundException {
        if (issueRepository.existsById(id)) {
            issueRepository.deleteById(id);
        } else {
            throw new NotFoundException("Issue id '%d' not found.".formatted(id));
        }
    }

    private static Issue toEntity(IssueModel model) {
        return new Issue(model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getCreatedAt(),
                model.getDueDate(),
                model.isClosed());
    }
}
