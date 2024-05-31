package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    public IssueDto createIssue(CreateIssueDto dto) {
        Issue issue = new Issue(null,
                dto.title(),
                dto.description(),
                Instant.now(),
                dto.dueDate(),
                false);
        Issue result = issueDao.save(issue);
        return toIssueDto(result);
    }

    public IssueDto updateIssue(long id, UpdateIssueDto dto) throws NotFoundException {
        Optional<Issue> opt = issueDao.findById(id);
        if (opt.isEmpty()) {
            throw new NotFoundException("Issue id '%d' not found.".formatted(id));
        }

        Issue issue = opt.get();
        issue.setTitle(dto.title());
        issue.setDescription(dto.description());
        issue.setDueDate(dto.dueDate());
        issue.setClosed(dto.closed());

        issue = issueDao.save(issue);
        return toIssueDto(issue);
    }

    public void deleteIssue(long id) throws NotFoundException {
        issueDao.deleteById(id);
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
