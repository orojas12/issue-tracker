package dev.oscarrojas.issuetracker.project.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.project.ProjectService;
import dev.oscarrojas.issuetracker.project.dto.ProjectDetails;

@RestController
@RequestMapping("/projects")
class ProjectController {

    private ProjectService projectService;

    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{projectId}")
    ProjectDetails getProjectDetails(@PathVariable("projectId") String projectId)
            throws NotFoundException {
        return projectService.getProject(projectId);
    }
}
