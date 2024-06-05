package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.issue.dto.CreateIssueData;
import dev.oscarrojas.issuetracker.issue.dto.IssueDetails;
import dev.oscarrojas.issuetracker.issue.dto.UpdateIssueData;

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

    public Optional<IssueDetails> getIssue(long id) {
        return issueDao.findById(id);
    }

    public List<IssueDetails> getIssueList(String projectId) {
        return issueDao.findAllByProjectId(projectId);
    }

    public IssueDetails createIssue(CreateIssueData data) {
        Issue issue = new Issue(null,
                data.title(),
                data.description(),
                Instant.now(),
                true,
                data.project());
        return issueDao.save(issue);
    }

    public IssueDetails updateIssue(long id, UpdateIssueData data) throws NotFoundException {
        Optional<IssueDetails> opt = issueDao.findById(id);
        if (opt.isEmpty()) {
            throw new NotFoundException("Issue id '%d' not found.".formatted(id));
        }

        Issue issue = new Issue(opt.get());
        issue.setTitle(data.title());
        issue.setDescription(data.description());
        issue.setOpen(data.open());

        return issueDao.save(issue);
    }

    public void deleteIssue(long id) throws NotFoundException {
        issueDao.deleteById(id);
    }

}
