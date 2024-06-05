package dev.oscarrojas.issuetracker.issue.dto;

import java.time.Instant;

public record PublicIssueDetails(long id, String title, String description,
        Instant createdAt, boolean isOpen) {
}
