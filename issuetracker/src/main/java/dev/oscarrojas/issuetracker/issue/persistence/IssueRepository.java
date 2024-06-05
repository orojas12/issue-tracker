package dev.oscarrojas.issuetracker.issue.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<JpaIssue, Long> {

    List<JpaIssue> findAllByProject_Id(String projectId);

}
