package dev.oscarrojas.issuetracker.project.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.oscarrojas.issuetracker.project.ProjectDao;
import dev.oscarrojas.issuetracker.project.dto.ProjectDetails;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;

@Repository
class JpaProjectDao implements ProjectDao {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private JpaProjectFactory projectFactory;

    JpaProjectDao(ProjectRepository projectRepository,
            UserRepository userRepository,
            JpaProjectFactory projectFactory) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectFactory = projectFactory;
    }

    @Override
    public Optional<ProjectDetails> findById(String projectId) {
        return projectRepository.findById(projectId)
                .map(project -> (ProjectDetails) project);
    }

    @Override
    public List<ProjectDetails> findAllByOwner(String userId) {
        UserModel user = userRepository.getReferenceById(userId);
        return projectRepository.findAllByOwner(user).stream()
                .map(project -> (ProjectDetails) project)
                .toList();
    }

    @Override
    public ProjectDetails save(ProjectDetails projectDetails) {
        JpaProject project = projectFactory.fromDetails(projectDetails);
        return projectRepository.save(project);
    }

    @Override
    public void deleteById(String projectId) {
        projectRepository.deleteById(projectId);
    }

}
