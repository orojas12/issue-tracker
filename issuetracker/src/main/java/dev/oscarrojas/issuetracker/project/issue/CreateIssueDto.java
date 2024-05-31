package dev.oscarrojas.issuetracker.project.issue;

import jakarta.annotation.Nullable;

import java.time.ZonedDateTime;

public record CreateIssueDto(
                String title,
                String description,
                @Nullable ZonedDateTime dueDate) {
}
