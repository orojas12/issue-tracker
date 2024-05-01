package dev.oscarrojas.issuetracker.project;

import java.time.Instant;

import dev.oscarrojas.issuetracker.team.TeamEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "project")
public class ProjectEntity {
    
    @Id
    private String id;
    private String name;
    private Instant creationDate;
    @OneToMany(fetch = FetchType.LAZY)
    private TeamEntity teams;

    public ProjectEntity() {}

    public ProjectEntity(String id, String name, Instant creationDate) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

}
