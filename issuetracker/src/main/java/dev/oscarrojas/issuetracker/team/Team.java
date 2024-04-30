package dev.oscarrojas.issuetracker.team;

import java.time.Instant;
import java.util.Set;

import dev.oscarrojas.issuetracker.exceptions.DuplicateUserException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;

public class Team {

    private String id;
    private String name;
    private Instant creationDate;
    private Set<User> members;

    public Team() {}

    public Team(String id, String name, Instant creationDate, Set<User> members) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.members = members;
    }

    public User getMember(String username) throws NotFoundException {
        User member = null;
        for (User user : members) {
            if (user.getUsername() == username) {
                member = user;
                break;
            }
        }
        if (member == null) {
            throw new NotFoundException(
                String.format(
                    "User '%s' not found in team '%s'",
                    username,
                    name
                )
            );
        }
        return member;
    }

    public void addMember(User user) throws DuplicateUserException {
        if (!members.contains(user)) {
            members.add(user);
        } else {
            throw new DuplicateUserException(
                String.format(
                    "User '%s' is already a member of team '%s'",
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
                    "User '%s' not found in team '%s'",
                    username,
                    name
                )
            );
        }
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
