package dev.oscarrojas.issuetracker.project.issue;

import java.time.Instant;
import java.time.ZonedDateTime;

public record IssueDto(
                Long id,
                String title,
                String description,
                Instant createdAt,
                ZonedDateTime dueDate,
                boolean closed) {
}
