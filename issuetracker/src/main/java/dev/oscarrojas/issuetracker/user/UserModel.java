package dev.oscarrojas.issuetracker.user;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "user")
public class UserModel {

    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    private Instant dateCreated;

    public UserModel() {}

    public UserModel(String id,
                     String username,
                     String firstName,
                     String lastName,
                     Instant dateCreated) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
