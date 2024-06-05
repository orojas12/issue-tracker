package dev.oscarrojas.issuetracker.issue.dto;

import java.time.ZonedDateTime;

public record UpdateIssueData(
        String title,
        String description,
        ZonedDateTime dueDate,
        boolean open) {
}
