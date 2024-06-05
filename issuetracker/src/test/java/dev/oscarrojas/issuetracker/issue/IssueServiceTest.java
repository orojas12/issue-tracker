package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.issue.dto.CreateIssueData;
import dev.oscarrojas.issuetracker.issue.dto.IssueDetails;
import dev.oscarrojas.issuetracker.issue.dto.UpdateIssueData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static dev.oscarrojas.issuetracker.issue.IssueTestUtils.issue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @Mock
    IssueDao issueDao;

    @Test
    void getIssuesList_shouldReturnListOfIssuesInProject() {

    }

    @Test
    void getIssue_shouldReturnIssue() {
        Issue issue = issue(1L);
        when(issueDao.findById(issue.getId())).thenReturn(Optional.of(issue));
        IssueService issueService = new IssueService(issueDao);

        Optional<IssueDetails> opt = issueService.getIssue(issue.getId());

        assertTrue(opt.isPresent());
        IssueDetails result = opt.get();
        assertEquals(issue.getId(), result.getId());
        assertEquals(issue.getTitle(), result.getTitle());
        assertEquals(issue.getDescription(), result.getDescription());
        assertEquals(issue.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void createIssue_returnsCreatedIssue() {
        CreateIssueData dto = new CreateIssueData("title", "desc", "project1");
        IssueDetails expected = issue(1L);
        when(issueDao.save(argThat((arg) -> {
            assertEquals(dto.title(), arg.getTitle());
            assertEquals(dto.description(), arg.getDescription());
            return true;
        }))).thenAnswer(i -> i.getArgument(0));

        IssueService service = new IssueService(issueDao);
        IssueDetails result = service.createIssue(dto);

        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getDescription(), result.getDescription());
        assertEquals(expected.isOpen(), result.isOpen());
    }

    @Test
    void updateIssue_updatesIssue() throws NotFoundException {
        UpdateIssueData data = new UpdateIssueData("new title",
                "new desc",
                null,
                true);
        Issue issue = issue(1L);
        when(issueDao.findById(issue.getId())).thenReturn(Optional.of(issue));
        when(issueDao.save(any(Issue.class))).thenAnswer(i -> i.getArgument(0));
        IssueService service = new IssueService(issueDao);
        IssueDetails result = service.updateIssue(issue.getId(), data);

        assertEquals(issue.getId(), result.getId());
        assertEquals(data.title(), result.getTitle());
        assertEquals(data.description(), result.getDescription());
        assertEquals(data.open(), result.isOpen());
    }

    @Test
    void deleteIssue_deletesIssue() throws NotFoundException {
        long id = 1L;
        doNothing().when(issueDao).deleteById(id);
        IssueService service = new IssueService(issueDao);

        service.deleteIssue(id);
    }

    static class TestIssueDao implements IssueDao {

        @Override
        public Optional<IssueDetails> findById(long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Issue save(IssueDetails issue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(long id) throws NotFoundException {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<IssueDetails> findAllByProjectId(String projectId) {
            throw new UnsupportedOperationException();
        }
    }
}
