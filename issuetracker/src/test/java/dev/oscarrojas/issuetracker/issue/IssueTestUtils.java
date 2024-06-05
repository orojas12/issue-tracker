package dev.oscarrojas.issuetracker.issue;

import static dev.oscarrojas.issuetracker.project.ProjectTestUtils.jpaProject;
import static dev.oscarrojas.issuetracker.util.RandomStringGenerator.getRandomString;

import java.time.Instant;

import dev.oscarrojas.issuetracker.issue.persistence.JpaIssue;

class IssueTestUtils {

    public static Issue issue(Long id) {
        return new Issue(
                id,
                "title",
                "desc",
                Instant.now(),
                true,
                getRandomString(8));
    }

    public static JpaIssue jpaIssue(Long id) {
        return new JpaIssue(
                null,
                "title",
                "desc",
                Instant.now(),
                true,
                jpaProject(getRandomString(8)));
    }

}
