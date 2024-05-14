package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.IntegrationTestConfig;
import dev.oscarrojas.issuetracker.IssueTracker;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
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

import static dev.oscarrojas.issuetracker.TestUtils.issue;
import static dev.oscarrojas.issuetracker.TestUtils.issueModel;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = {IssueTracker.class, IntegrationTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({IssueDaoJpa.class})
@Sql("/sqlite/schema.sql")
public class IssueDaoJpaIT {

    @Autowired
    IssueDao issueDao;

    @Autowired
    TestEntityManager testEm;

    @Autowired
    EntityManager em;

    @Test
    void findAll_findsAllIssues() {
        IssueModel issue1 = testEm.persistFlushFind(issueModel(null));
        IssueModel issue2 = testEm.persistFlushFind(issueModel(null));

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
        IssueModel issue1 = testEm.persistFlushFind(issueModel(null));

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

    @Test
    void save_returnsSavedIssue() {
        Issue issue = issue(null);

        Issue result = issueDao.save(issue);

        assertNotNull(result.getId());
        assertEquals(issue.getTitle(), result.getTitle());
        assertEquals(issue.getDescription(), result.getDescription());
        assertEquals(issue.getCreatedAt(), result.getCreatedAt());
        assertEquals(issue.getDueDate(), result.getDueDate());
        assertEquals(issue.isClosed(), result.isClosed());
    }

    @Test
    void save_savesNewIssueToDatabase() {
        Issue issue = issue(null);

        Issue result = issueDao.save(issue);

        List<IssueModel> models = em.createQuery("select i from IssueModel i", IssueModel.class)
                .getResultList();

        assertEquals(1, models.size());
        IssueModel model = models.getFirst();
        assertNotNull(model);
        assertEquals(issue.getTitle(), model.getTitle());
        assertEquals(issue.getDescription(), model.getDescription());
        assertEquals(issue.getCreatedAt(), model.getCreatedAt());
        assertEquals(issue.getDueDate(), model.getDueDate());
        assertEquals(issue.isClosed(), model.isClosed());
    }

    @Test
    void save_updatesIssueIfPkIsNotNull() {
        IssueModel model = testEm.persistAndFlush(issueModel(1L));
        Issue issue = new Issue(model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getCreatedAt(),
                model.getDueDate(),
                model.isClosed());

        issue.setTitle("new title");
        issue.setDescription("new description");

        Issue result = issueDao.save(issue);


        List<IssueModel> models = em.createQuery("select i from IssueModel i", IssueModel.class)
                .getResultList();
        // should not create a new issue if it has an id
        assertEquals(1, models.size());
        model = testEm.find(IssueModel.class, result.getId());
        assertNotNull(model);
        assertEquals(issue.getTitle(), model.getTitle());
        assertEquals(issue.getDescription(), model.getDescription());
        assertEquals(issue.getCreatedAt(), model.getCreatedAt());
        assertEquals(issue.getDueDate(), model.getDueDate());
        assertEquals(issue.isClosed(), model.isClosed());
    }

    @Test
    void deleteById_throwsNotFoundIfNotFound() {
        assertThrows(NotFoundException.class, () -> issueDao.deleteById(1L));
    }

    @Test
    void deleteById_deletesIssueFromDatabase() throws NotFoundException {
        IssueModel model = testEm.persistAndFlush(issueModel(1L));

        issueDao.deleteById(model.getId());

        IssueModel result = testEm.find(IssueModel.class, model.getId());
        assertNull(result);
    }

}
