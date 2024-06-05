package dev.oscarrojas.issuetracker.user;

import dev.oscarrojas.issuetracker.data.SQLiteInstantConverter;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "account")
public class UserModel {

    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    @Convert(converter = SQLiteInstantConverter.class)
    private Instant createdAt;

    public UserModel() {
    }

    public UserModel(String id,
            String username,
            String firstName,
            String lastName,
            Instant createdAt) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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
