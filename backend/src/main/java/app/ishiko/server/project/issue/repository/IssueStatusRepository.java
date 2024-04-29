package app.ishiko.server.project.issue.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import app.ishiko.server.project.issue.model.IssueStatus;

public interface IssueStatusRepository
        extends JpaRepository<IssueStatus, Integer> {
    List<IssueStatus> findAllByProjectId(String projectId);
}
