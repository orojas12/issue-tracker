package dev.oscarrojas.issuetracker.project;

import java.util.List;
import java.util.Optional;

import dev.oscarrojas.issuetracker.project.dto.ProjectDetails;

public interface ProjectDao {

    Optional<ProjectDetails> findById(String projectId);

    List<ProjectDetails> findAllByOwner(String userId);

    ProjectDetails save(ProjectDetails project);

    void deleteById(String projectId);

}
