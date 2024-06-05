package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.IntegrationTestConfig;
import dev.oscarrojas.issuetracker.IssueTracker;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.issue.dto.IssueDetails;
import dev.oscarrojas.issuetracker.issue.persistence.JpaIssue;
import dev.oscarrojas.issuetracker.issue.persistence.JpaIssueDao;
import dev.oscarrojas.issuetracker.issue.persistence.JpaIssueFactory;
import dev.oscarrojas.issuetracker.project.persistence.JpaProject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static dev.oscarrojas.issuetracker.issue.IssueTestUtils.jpaIssue;
import static dev.oscarrojas.issuetracker.project.ProjectTestUtils.jpaProject;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = { IssueTracker.class, IntegrationTestConfig.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ JpaIssueDao.class, JpaIssueFactory.class })
@Sql("/sqlite/schema.sql")
public class IssueDaoJpaIT {

    @Autowired
    IssueDao issueDao;

    @Autowired
    TestEntityManager testEm;

    @Autowired
    EntityManager em;

    private JpaIssue createIssue(Long id) {
        JpaIssue issue = jpaIssue(id);
        testEm.persist(issue.getProject().getOwner());
        testEm.persist(issue.getProject());
        return testEm.persistAndFlush(issue);
    }

    @Test
    void findById_returnsIssue() {
        JpaIssue model = createIssue(null);

        Optional<IssueDetails> opt = issueDao.findById(model.getId());

        assertTrue(opt.isPresent());
        Issue result = new Issue(opt.get());
        assertEquals(model.getTitle(), result.getTitle());
        assertEquals(model.getDescription(), result.getDescription());
        assertEquals(model.getCreatedAt(), result.getCreatedAt());
        assertEquals(model.isOpen(), result.isOpen());
    }

    @Test
    void findById_returnsEmptyIfNotFound() {
        long id = 1L;

        Optional<IssueDetails> opt = issueDao.findById(id);

        assertTrue(opt.isEmpty());
    }

    @Test
    void save_returnsSavedIssue() {
        JpaProject project = jpaProject(null);
        testEm.persist(project.getOwner());
        project = testEm.persistAndFlush(project);
        Issue issue = new Issue(1L, "title", "desc", Instant.now(), true, project.getId());

        IssueDetails result = issueDao.save(issue);

        assertNotNull(result.getId());
        assertEquals(issue.getTitle(), result.getTitle());
        assertEquals(issue.getDescription(), result.getDescription());
        assertEquals(issue.getCreatedAt(), result.getCreatedAt());
        assertEquals(issue.isOpen(), result.isOpen());
    }

    @Test
    void save_savesNewIssueToDatabase() {
        JpaProject project = jpaProject(null);
        testEm.persist(project.getOwner());
        project = testEm.persistAndFlush(project);
        Issue issue = new Issue(1L, "title", "desc", Instant.now(), true, project.getId());

        issueDao.save(issue);

        List<JpaIssue> models = em.createQuery("select i from JpaIssue i", JpaIssue.class)
                .getResultList();

        assertEquals(1, models.size());
        JpaIssue model = models.getFirst();
        assertNotNull(model);
        assertEquals(issue.getTitle(), model.getTitle());
        assertEquals(issue.getDescription(), model.getDescription());
        assertEquals(issue.getCreatedAt(), model.getCreatedAt());
        assertEquals(issue.isOpen(), model.isOpen());
    }

    @Test
    void save_updatesIssueIfPkIsNotNull() {
        JpaIssue model = testEm.persistAndFlush(createIssue(1L));
        Issue issue = new Issue(model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getCreatedAt(),
                model.isOpen(),
                model.getProjectId());

        issue.setTitle("new title");
        issue.setDescription("new description");

        IssueDetails result = issueDao.save(issue);

        List<JpaIssue> models = em.createQuery("select i from JpaIssue i", JpaIssue.class)
                .getResultList();
        // should not create a new issue if it has an id
        assertEquals(1, models.size());
        model = testEm.find(JpaIssue.class, result.getId());
        assertNotNull(model);
        assertEquals(issue.getTitle(), model.getTitle());
        assertEquals(issue.getDescription(), model.getDescription());
        assertEquals(issue.getCreatedAt(), model.getCreatedAt());
        assertEquals(issue.isOpen(), model.isOpen());
    }

    @Test
    void deleteById_throwsNotFoundIfNotFound() {
        assertThrows(NotFoundException.class, () -> issueDao.deleteById(1L));
    }

    @Test
    void deleteById_deletesIssueFromDatabase() throws NotFoundException {
        JpaIssue model = testEm.persistAndFlush(createIssue(1L));

        issueDao.deleteById(model.getId());

        JpaIssue result = testEm.find(JpaIssue.class, model.getId());
        assertNull(result);
    }

}
