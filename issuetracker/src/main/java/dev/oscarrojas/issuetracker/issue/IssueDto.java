package dev.oscarrojas.issuetracker.issue;

import java.time.Instant;

public record IssueDto(
        Long id,
        String title,
        String description,
        Instant createdAt,
        Instant dueDate,
        boolean closed
) {
}
