package dev.oscarrojas.issuetracker.issue;

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
        return null;
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
