package dev.oscarrojas.issuetracker.project;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.project.dto.CreateProjectData;
import dev.oscarrojas.issuetracker.project.dto.ProjectDetails;
import dev.oscarrojas.issuetracker.project.dto.UpdateProjectData;

@Service
public class ProjectService {

    private ProjectDao projectDao;

    public ProjectService(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public ProjectDetails getProject(String projectId) throws NotFoundException {
        Optional<ProjectDetails> opt = projectDao.findById(projectId);
        return opt.orElseThrow(
                () -> new NotFoundException(
                        "Project id '%s' not found.".formatted(projectId)));
    }

    public ProjectDetails createProject(CreateProjectData data, String userId) {
        Project project = new Project(
                null,
                data.name(),
                data.description(),
                Instant.now(),
                userId);
        return projectDao.save(project);
    }

    public ProjectDetails updateProject(UpdateProjectData data, String userId)
            throws NotFoundException {
        Optional<ProjectDetails> opt = projectDao.findById(data.id());

        Project project = new Project(
                opt.orElseThrow(() -> new NotFoundException(
                        "Project id '%s' not found.".formatted(data.id()))));

        return projectDao.save(project);
    }

    public void deleteProject(String projectId) {
        projectDao.deleteById(projectId);
    }

}
