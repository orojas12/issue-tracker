package dev.oscarrojas.issuetracker.user;

import dev.oscarrojas.issuetracker.team.TeamModel;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "user")
public class UserModel {

    @Id
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_role")
    private Set<RoleModel> roles;
    private Instant dateCreated;
    @ManyToMany(mappedBy = "members")
    private Set<TeamModel> teams;

    public UserModel() {}

    public UserModel(String username, String password, Set<RoleModel> roles, Instant dateCreated) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleModel> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<RoleModel> roles) {
        this.roles = roles;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

}
