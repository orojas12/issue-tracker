package dev.oscarrojas.issuetracker.issue;

import java.time.ZonedDateTime;

public record UpdateIssueDto(
        String title,
        String description,
        ZonedDateTime dueDate,
        boolean closed
) {
}
