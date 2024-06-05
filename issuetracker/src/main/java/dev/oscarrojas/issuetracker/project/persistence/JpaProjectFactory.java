package dev.oscarrojas.issuetracker.project.persistence;

import org.springframework.stereotype.Service;

import dev.oscarrojas.issuetracker.project.dto.ProjectDetails;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;

@Service
public class JpaProjectFactory {

    private UserRepository userRepository;

    public JpaProjectFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JpaProject fromDetails(ProjectDetails details) {
        JpaProject project = new JpaProject();
        project.setId(details.getId());
        project.setName(details.getName());
        project.setDescription(details.getDescription());
        project.setCreatedAt(details.getCreatedAt());
        UserModel user = userRepository.getReferenceById(details.getOwnerId());
        project.setOwner(user);
        return project;
    }
}
