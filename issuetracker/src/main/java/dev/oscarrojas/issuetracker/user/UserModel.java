package dev.oscarrojas.issuetracker.user;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "user")
public class UserModel {

    @Id
    private String username;
    private String password;
    private Instant dateCreated;

    public UserModel() {}

    public UserModel(String username, String password, Instant dateCreated) {
        this.username = username;
        this.password = password;
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

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

}
