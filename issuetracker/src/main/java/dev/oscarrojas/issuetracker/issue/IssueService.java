package dev.oscarrojas.issuetracker.issue;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    private final IssueDao issueDao;

    public IssueService(IssueDao issueDao) {
        this.issueDao = issueDao;
    }

    public List<IssueDto> getAllIssues() {
        List<Issue> issues = issueDao.findAll();
        return issues.stream().map(IssueService::toIssueDto).toList();
    }

    public Optional<IssueDto> getIssue(long id) {
        Optional<Issue> issue = issueDao.findById(id);
        return issue.map(IssueService::toIssueDto);
    }

    private static IssueDto toIssueDto(Issue issue) {
        return new IssueDto(issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getCreatedAt(),
                issue.getDueDate(),
                issue.isClosed());
    }
}
