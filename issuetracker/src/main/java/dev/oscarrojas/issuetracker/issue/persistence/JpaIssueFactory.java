package dev.oscarrojas.issuetracker.issue.persistence;

import org.springframework.stereotype.Repository;

import dev.oscarrojas.issuetracker.issue.dto.IssueDetails;
import dev.oscarrojas.issuetracker.project.persistence.JpaProject;
import dev.oscarrojas.issuetracker.project.persistence.ProjectRepository;

@Repository
public class JpaIssueFactory {

    private ProjectRepository projectRepository;

    public JpaIssueFactory(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public JpaIssue fromDetails(IssueDetails details) {
        JpaIssue issue = new JpaIssue();
        issue.setId(details.getId());
        issue.setTitle(details.getTitle());
        issue.setDescription(details.getDescription());
        issue.setCreatedAt(details.getCreatedAt());
        issue.setOpen(details.isOpen());
        JpaProject project = projectRepository.getReferenceById(details.getProjectId());
        issue.setProject(project);
        return issue;
    }
}
