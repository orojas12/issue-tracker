package app.ishiko.server.project.issue.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import app.ishiko.server.project.issue.model.IssueLabel;

public interface IssueLabelRepository
        extends JpaRepository<IssueLabel, Integer> {
    List<IssueLabel> findAllByProjectId(String projectId);
}
