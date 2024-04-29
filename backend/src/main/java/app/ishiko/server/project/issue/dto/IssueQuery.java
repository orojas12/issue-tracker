package app.ishiko.server.project.issue.dto;

import java.util.Set;
import jakarta.annotation.Nullable;

public record IssueQuery(
    String projectId,
    @Nullable Set<String> labels, 
    @Nullable Set<String> statuses) {
}
