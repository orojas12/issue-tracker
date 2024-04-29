package app.ishiko.server.project;

import java.util.Optional;
import org.springframework.stereotype.Service;
import app.ishiko.server.exception.NotFoundException;
import app.ishiko.server.project.issue.dto.IssueLabelDto;
import app.ishiko.server.project.issue.dto.IssueStatusDto;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public boolean projectExists(String projectId, String username) {
        return projectRepository.existsByIdAndOwner_Username(projectId, username);
    }

    public ProjectDto getProjectData(String projectId, String username)
            throws NotFoundException {
        Optional<Project> projectOpt = projectRepository
                .findByIdAndOwner_Username(projectId, username);

        if (projectOpt.isEmpty()) {
            throw new NotFoundException("Project id " + projectId
                    + " not found for user " + username);
        }

        Project project = projectOpt.get();
        ProjectDto dto = new ProjectDto(project.getId(), project.getName(),
                project.getDescription(), project.getOwner().getUsername());

        dto.setStatuses(project.getIssueStatuses().stream()
                .map((status -> new IssueStatusDto(status.getId(),
                        status.getName())))
                .toList());
        dto.setLabels(project.getIssueLabels().stream()
                .map(label -> new IssueLabelDto(label.getId(), label.getName()))
                .toList());

        return dto;
    }
}
