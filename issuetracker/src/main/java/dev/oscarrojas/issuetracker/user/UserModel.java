package dev.oscarrojas.issuetracker.user;

import java.time.Instant;
import java.util.Set;

import dev.oscarrojas.issuetracker.team.TeamModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class UserModel {

    @Id
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role")
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
