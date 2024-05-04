package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.user.UserModel;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "team")
public class TeamModel {
    
    @Id
    private String id;
    private String name;
    private Instant dateCreated;
    @ManyToMany
    @JoinTable(name = "team_member")
    private Set<UserModel> members;

    public TeamModel() {}

    public TeamModel(
            String id, 
            String name, 
            Instant dateCreated, 
            Set<UserModel> members) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.members = members;
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

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<UserModel> getMembers() {
        return members;
    }

    public void setMembers(Set<UserModel> members) {
        this.members = members;
    }

}
