package dev.oscarrojas.issuetracker.team;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "team")
public class TeamEntity {
    
    @Id
    private String id;
    private String name;
    private Instant creationDate;

    public TeamEntity() {}

    public TeamEntity(String id, String name, Instant creationDate) {
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
