package app.ishiko.server.project;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.provisioning.UserDetailsManager;

import app.ishiko.server.project.Project;
import app.ishiko.server.project.ProjectRepository;
import app.ishiko.server.project.issue.dto.IssueQuery;
import app.ishiko.server.project.issue.model.Issue;
import app.ishiko.server.project.issue.repository.IssueRepository;
import app.ishiko.server.project.issue.repository.IssueSpecification;
import app.ishiko.server.user.AppUser;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class IssueSpecificationIT {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    UserDetailsManager manager;

    private void setUpTestData() {
        UserBuilder user = User.builder();
        user.username("test_user").password("{noop}password")
                .authorities("ROLE_USER");
        manager.createUser(user.build());
        AppUser owner = new AppUser("test_user");
        Project project = ProjectFaker.createProject(owner);
        projectRepository.save(project);
    }

    private void tearDownTestData() {
        manager.deleteUser("test_user");
    }

    @Test
    public void findAll_IssueSpec_returnsIssuesMatchingSpec() {
        setUpTestData();

        IssueQuery query = new IssueQuery("project_test", Set.of("label2"), null);
        IssueSpecification spec = new IssueSpecification(query);
        List<Issue> issues = issueRepository.findAll(spec);
        assertThat(issues.size()).isEqualTo(2);
        assertThat(issues.get(0).getLabel().get().getName()).isEqualTo("label2");
        assertThat(issues.get(1).getLabel().get().getName()).isEqualTo("label2");

        tearDownTestData();
    }

}
