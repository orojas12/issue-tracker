package dev.oscarrojas.issuetracker.issue.dto;

import java.time.Instant;

public interface IssueDetails {

    Long getId();

    String getTitle();

    String getDescription();

    Instant getCreatedAt();

    boolean isOpen();

    String getProjectId();

    boolean isNew();

}
