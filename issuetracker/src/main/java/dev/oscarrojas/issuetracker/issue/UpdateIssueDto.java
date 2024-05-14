package dev.oscarrojas.issuetracker.issue;

import java.time.Instant;

public record UpdateIssueDto(
        String title,
        String description,
        Instant dueDate,
        boolean closed
) {
}
