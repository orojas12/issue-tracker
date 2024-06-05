package dev.oscarrojas.issuetracker.project.persistence;

import java.time.Instant;

import dev.oscarrojas.issuetracker.project.dto.ProjectDetails;
import dev.oscarrojas.issuetracker.user.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "project")
public class JpaProject implements ProjectDetails {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private Instant createdAt;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private UserModel owner;

    JpaProject() {
    }

    public JpaProject(
            String id,
            String name,
            String description,
            Instant createdAt,
            UserModel owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.owner = owner;
    }

    @Override
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getOwnerId() {
        return owner.getId();
    }

    public UserModel getOwner() {
        return owner;
    }

    void setOwner(UserModel user) {
        this.owner = user;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

}
