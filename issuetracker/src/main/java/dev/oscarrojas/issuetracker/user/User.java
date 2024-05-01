package dev.oscarrojas.issuetracker.user;

import java.time.Instant;
import java.util.Set;

public class User {

    private String username;
    private Set<String> roles;
    private Instant dateCreated;

    public User() {}

    public User(String username, Set<String> roles,
            Instant dateCreated) {
        this.username = username;
        this.roles = roles;
        this.dateCreated = dateCreated;
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

}
