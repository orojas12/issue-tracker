package dev.oscarrojas.issuetracker.project.issue;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record CreateIssueRequest(
                String title,
                String description,
                @Nullable LocalDateTime dueDate,
                @Nullable ZoneId dueDateTimeZone) {
}
