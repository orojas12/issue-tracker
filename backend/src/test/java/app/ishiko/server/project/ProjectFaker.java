package app.ishiko.server.project;

import java.time.Instant;
import java.util.List;

import app.ishiko.server.project.Project;
import app.ishiko.server.project.issue.model.Issue;
import app.ishiko.server.project.issue.model.IssueLabel;
import app.ishiko.server.project.issue.model.IssueStatus;
import app.ishiko.server.user.AppUser;

public class ProjectFaker {
    
    public static Project createProject(AppUser user) {
        Project project = new Project("test_project", "name", "desc", user);
        List<IssueStatus> statuses =
                List.of(new IssueStatus("in progress", project),
                        new IssueStatus("done", project));
        List<IssueLabel> labels = 
            List.of(new IssueLabel("label1", project), new IssueLabel("label2", project));
        List<Issue> issues = List.of(
            new Issue("subject", "desc", Instant.now(), 
                null, project, user, null, labels.get(0)),
            new Issue("subject", "desc", Instant.now(), 
                null, project, user, null, labels.get(1)),
            new Issue("subject", "desc", Instant.now(), 
                null, project, user, null, labels.get(1))
        );
        project.setIssueLabels(labels);
        project.setIssueStatuses(statuses);
        project.setIssues(issues);
        return project;
    }    
}
