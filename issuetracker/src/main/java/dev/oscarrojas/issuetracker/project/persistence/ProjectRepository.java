package dev.oscarrojas.issuetracker.project.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.oscarrojas.issuetracker.user.UserModel;

@Repository
public interface ProjectRepository extends JpaRepository<JpaProject, String> {

    List<JpaProject> findAllByOwner(UserModel owner);

}
