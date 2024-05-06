package dev.oscarrojas.issuetracker.user;

import java.time.Instant;

public class User {

    private String username;
    private Instant dateCreated;

    public User() {}

    public User(String username, Instant dateCreated) {
        this.username = username;
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

}
