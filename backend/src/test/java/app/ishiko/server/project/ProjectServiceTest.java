package app.ishiko.server.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import app.ishiko.server.exception.NotFoundException;
import app.ishiko.server.project.Project;
import app.ishiko.server.project.ProjectDto;
import app.ishiko.server.project.ProjectRepository;
import app.ishiko.server.project.ProjectService;
import app.ishiko.server.project.issue.model.IssueLabel;
import app.ishiko.server.project.issue.model.IssueStatus;
import app.ishiko.server.project.issue.repository.IssueLabelRepository;
import app.ishiko.server.project.issue.repository.IssueStatusRepository;
import app.ishiko.server.user.AppUser;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private IssueStatusRepository statusRepository;
    @Mock
    private IssueLabelRepository labelRepository;
    @Mock
    private ProjectRepository projectRepository;

    ProjectService getProjectService() {
        return new ProjectService(projectRepository);
    }

    @Test
    void getProjectData_ProjectIdAndUsername_returnsProjectDto()
            throws NotFoundException {
        AppUser user = new AppUser("john");
        Project project = new Project("project_1", "name", "description", user);
        List<IssueStatus> statuses =
                List.of(new IssueStatus(1, "status_1", project));
        List<IssueLabel> labels =
                List.of(new IssueLabel(1, "label_1", project));
        project.setIssueStatuses(statuses);
        project.setIssueLabels(labels);
        when(projectRepository.findByIdAndOwner_Username(project.getId(),
                user.getUsername())).thenReturn(Optional.of(project));

        ProjectService service = getProjectService();
        ProjectDto dto =
                service.getProjectData(project.getId(), user.getUsername());

        assertThat(dto.getId()).isEqualTo(project.getId());
        assertThat(dto.getName()).isEqualTo(project.getName());
        assertThat(dto.getDescription()).isEqualTo(project.getDescription());
        assertThat(dto.getOwner()).isEqualTo(project.getOwner().getUsername());
        assertThat(dto.getStatuses().size()).isEqualTo(statuses.size());
        assertThat(dto.getLabels().size()).isEqualTo(labels.size());
        for (int i = 0; i < statuses.size(); i++) {
            assertThat(dto.getStatuses().get(i).getId())
                    .isEqualTo(statuses.get(i).getId());
            assertThat(dto.getStatuses().get(i).getName())
                    .isEqualTo(statuses.get(i).getName());
        }
        for (int i = 0; i < labels.size(); i++) {
            assertThat(dto.getLabels().get(i).getId())
                    .isEqualTo(labels.get(i).getId());
            assertThat(dto.getLabels().get(i).getName())
                    .isEqualTo(labels.get(i).getName());
        }
    }

}
