package app.ishiko.server.project;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    Optional<Project> findByIdAndOwner_Username(String projectId,
            String username);

    boolean existsByIdAndOwner_Username(String projectId, String username);
}
