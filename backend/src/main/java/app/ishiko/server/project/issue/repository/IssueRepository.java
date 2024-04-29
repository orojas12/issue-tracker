package app.ishiko.server.project.issue.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import app.ishiko.server.project.issue.model.Issue;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer>, JpaSpecificationExecutor<Issue> {

    List<Issue> findAllByProject_IdOrderByCreateDateDesc(String id, Pageable pageable);

}
