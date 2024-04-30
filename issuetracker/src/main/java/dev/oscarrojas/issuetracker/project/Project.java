package dev.oscarrojas.issuetracker.project;

import java.time.Instant;
import java.util.Set;

import dev.oscarrojas.issuetracker.exceptions.DuplicateUserException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;

public class Project {

    private String name;
    private Instant creationDate;
    private Set<User> members;

    public Project() {}

    public Project(String name, Instant creationDate, Set<User> members) {
        this.name = name;
        this.creationDate = creationDate;
        this.members = members;
    }
    
    public void addMember(User user) throws DuplicateUserException {
        if (!members.contains(user)) {
            members.add(user);
        } else {
            throw new DuplicateUserException(
                String.format(
                    "User '%s' is already a member of project '%s'",
                    user.getUsername(),
                    name
                )
            );
        }
    }

    public void removeMember(String username) throws NotFoundException {
        boolean found = members.removeIf((user) -> user.getUsername() == username);
        if (!found) {
            throw new NotFoundException(
                String.format(
                    "User '%s' not found in project '%s'",
                    username,
                    name
                )
            );
        }
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

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

}
