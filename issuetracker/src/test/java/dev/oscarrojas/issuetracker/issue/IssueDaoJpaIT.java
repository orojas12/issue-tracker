package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.IntegrationTestConfig;
import dev.oscarrojas.issuetracker.IssueTracker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static dev.oscarrojas.issuetracker.TestUtils.issueModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = {IssueTracker.class, IntegrationTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({IssueDaoJpa.class})
@Sql("/sqlite/schema.sql")
public class IssueDaoJpaIT {

    @Autowired
    IssueDao issueDao;

    @Autowired
    TestEntityManager em;

    @Test
    void findAll_findsAllIssues() {
        IssueModel issue1 = em.persistFlushFind(issueModel(null));
        IssueModel issue2 = em.persistFlushFind(issueModel(null));

        List<Issue> issues = issueDao.findAll();

        assertEquals(2, issues.size());

        boolean hasIssue1 = false;
        boolean hasIssue2 = false;

        for (Issue issue : issues) {
            if (issue.getId().equals(issue1.getId())) {
                hasIssue1 = true;
                assertEquals(issue1.getTitle(), issue.getTitle());
                assertEquals(issue1.getDescription(), issue.getDescription());
                assertEquals(issue1.getCreatedAt(), issue.getCreatedAt());
                assertEquals(issue1.getDueDate(), issue.getDueDate());
                assertEquals(issue1.isClosed(), issue.isClosed());
            } else if (issue.getId().equals(issue2.getId())) {
                hasIssue2 = true;
                assertEquals(issue2.getTitle(), issue.getTitle());
                assertEquals(issue2.getDescription(), issue.getDescription());
                assertEquals(issue2.getCreatedAt(), issue.getCreatedAt());
                assertEquals(issue2.getDueDate(), issue.getDueDate());
                assertEquals(issue2.isClosed(), issue.isClosed());
            }
        }

        assertTrue(hasIssue1 && hasIssue2);
    }

    @Test
    void findById_returnsIssue() {
        IssueModel issue1 = em.persistFlushFind(issueModel(null));

        Optional<Issue> opt = issueDao.findById(issue1.getId());

        assertTrue(opt.isPresent());
        Issue result = opt.get();
        assertEquals(issue1.getTitle(), result.getTitle());
        assertEquals(issue1.getDescription(), result.getDescription());
        assertEquals(issue1.getCreatedAt(), result.getCreatedAt());
        assertEquals(issue1.getDueDate(), result.getDueDate());
        assertEquals(issue1.isClosed(), result.isClosed());
    }

    @Test
    void findById_returnsEmptyIfNotFound() {
        long id = 1L;

        Optional<Issue> opt = issueDao.findById(id);

        assertTrue(opt.isEmpty());
    }
}
