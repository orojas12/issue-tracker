package dev.oscarrojas.issuetracker.user;

import java.time.Instant;

public class UserBuilder {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Instant dateCreated;

    public UserBuilder id(String id) {
        this.id = id;
        return this;
    }

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public User build() {
        User user = new User();

        if (dateCreated != null) {
            user.setDateCreated(dateCreated);
        } else {
            user.setDateCreated(Instant.now());
        }

        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return user;
    }
}
