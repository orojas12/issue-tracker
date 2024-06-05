package dev.oscarrojas.issuetracker.project.dto;

import java.time.Instant;

import dev.oscarrojas.issuetracker.core.EntityDetails;

public interface ProjectDetails extends EntityDetails {

    String getId();

    String getName();

    String getDescription();

    Instant getCreatedAt();

    String getOwnerId();

}
