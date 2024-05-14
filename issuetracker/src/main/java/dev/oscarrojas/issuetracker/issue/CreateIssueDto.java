package dev.oscarrojas.issuetracker.issue;

import java.time.Instant;

public record CreateIssueDto(
        String title,
        String description,
        Instant dueDate
) {
}
