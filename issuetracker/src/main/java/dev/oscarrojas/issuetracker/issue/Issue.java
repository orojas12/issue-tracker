package dev.oscarrojas.issuetracker.issue;

import jakarta.annotation.Nullable;

import java.time.Instant;

import dev.oscarrojas.issuetracker.issue.dto.IssueDetails;

public class Issue implements IssueDetails {

    @Nullable
    private Long id;
    private String title;
    @Nullable
    private String description;
    private Instant createdAt;
    private boolean open;
    private String projectId;

    private Issue() {
    }

    Issue(IssueDetails details) {
        this.id = details.getId();
        this.title = details.getTitle();
        this.description = details.getDescription();
        this.createdAt = details.getCreatedAt();
        this.open = details.isOpen();
        this.projectId = details.getProjectId();
    }

    Issue(Long id, String title, String description,
            Instant createdAt, boolean open, String projectId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.open = open;
        this.projectId = projectId;
    }

    void close() {
        this.open = false;
    }

    void reopen() {
        this.open = true;
    }

    @Override
    public Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isClosed() {
        return !open;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

}
