package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Team {

    private String id;
    private String name;
    private Instant dateCreated;
    private HashSet<TeamMember> members;

    public Team() {
    }

    public Team(String id, String name, Instant dateCreated, HashSet<TeamMember> members) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.members = members;
    }

    public boolean hasMember(String username) {
        for (TeamMember member : members) {
            if (member.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void addMember(TeamMember teamMember) throws DuplicateElementException {
        if (!members.contains(teamMember)) {
            members.add(teamMember);
        } else {
            throw new DuplicateElementException(
                String.format(
                    "User '%s' is already a member of team '%s'",
                    teamMember.username(),
                    name
                )
            );
        }
    }

    public Optional<TeamMember> getMember(String username) {
        TeamMember teamMember = null;
        for (TeamMember member : members) {
            if (member.username().equals(username)) {
                teamMember = member;
                break;
            }
        }
        return Optional.ofNullable(teamMember);
    }

    public void removeMember(String username) throws NotFoundException {
        boolean found = members.removeIf((member) -> member.username().equals(username));
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

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant creationDate) {
        this.dateCreated = creationDate;
    }

    public Set<TeamMember> getMembers() {
        return members;
    }

    public void setMembers(HashSet<TeamMember> members) {
        this.members = members;
    }

}
