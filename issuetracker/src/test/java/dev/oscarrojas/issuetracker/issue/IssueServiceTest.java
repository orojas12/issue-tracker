package dev.oscarrojas.issuetracker.issue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static dev.oscarrojas.issuetracker.TestUtils.issue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @Mock
    IssueDao issueDao;

    @Test
    void getAllIssues_shouldReturnAllIssues() {
        Issue issue1 = issue(1L);
        Issue issue2 = issue(2L);
        when(issueDao.findAll()).thenReturn(List.of(issue1, issue2));
        IssueService issueService = new IssueService(issueDao);

        List<IssueDto> results = issueService.getAllIssues();

        IssueDto result1 = results.get(0);
        IssueDto result2 = results.get(1);
        assertEquals(2, results.size());
        assertEquals(issue1.getId(), result1.id());
        assertEquals(issue1.getTitle(), result1.title());
        assertEquals(issue1.getDescription(), result1.description());
        assertEquals(issue1.getCreatedAt(), result1.createdAt());
        assertEquals(issue1.getDueDate(), result1.dueDate());
        assertEquals(issue1.isClosed(), result1.closed());
        assertEquals(issue2.getId(), result2.id());
        assertEquals(issue2.getTitle(), result2.title());
        assertEquals(issue2.getDescription(), result2.description());
        assertEquals(issue2.getCreatedAt(), result2.createdAt());
        assertEquals(issue2.getDueDate(), result2.dueDate());
    }

    @Test
    void getIssue_shouldReturnIssue() {
        Issue issue = issue(1L);
        when(issueDao.findById(issue.getId())).thenReturn(Optional.of(issue));
        IssueService issueService = new IssueService(issueDao);

        Optional<IssueDto> opt = issueService.getIssue(issue.getId());

        assertTrue(opt.isPresent());
        IssueDto result = opt.get();
        assertEquals(issue.getId(), result.id());
        assertEquals(issue.getTitle(), result.title());
        assertEquals(issue.getDescription(), result.description());
        assertEquals(issue.getCreatedAt(), result.createdAt());
        assertEquals(issue.getDueDate(), result.dueDate());
    }

    static class TestIssueDao implements IssueDao {

        @Override
        public List<Issue> findAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Issue> findById(long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Issue save(Issue issue) {
            throw new UnsupportedOperationException();
        }
    }
}
