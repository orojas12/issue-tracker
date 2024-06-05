package dev.oscarrojas.issuetracker.project;

import java.time.Instant;

import dev.oscarrojas.issuetracker.project.dto.ProjectDetails;

public class Project implements ProjectDetails {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private String ownerId;

    Project(ProjectDetails details) {
        this.id = details.getId();
        this.name = details.getName();
        this.description = details.getDescription();
        this.createdAt = details.getCreatedAt();
        this.ownerId = details.getOwnerId();
    }

    Project(String id, String name,
            String description, Instant createdAt,
            String ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.ownerId = ownerId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    void setId(String id) {
        this.id = id;
    }

    void setName(String name) {
        this.name = name;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    void setOwnerId(String userId) {
        this.ownerId = userId;
    }

}
