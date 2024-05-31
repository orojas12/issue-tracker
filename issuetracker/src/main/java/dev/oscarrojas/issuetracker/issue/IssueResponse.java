package dev.oscarrojas.issuetracker.issue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record IssueResponse(
        long id,
        String title,
        String description,
        Instant createdAt,
        LocalDateTime dueDate,
        ZoneId dueDateTimeZone,
        boolean closed
) {
}
