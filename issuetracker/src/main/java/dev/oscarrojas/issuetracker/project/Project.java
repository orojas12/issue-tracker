package dev.oscarrojas.issuetracker.project;

import java.time.Instant;
import java.util.Set;

import dev.oscarrojas.issuetracker.team.Team;

public class Project {

    private String name;
    private Instant creationDate;
    private Set<Team> teams;

    public Project() {}

    public Project(String name, Instant creationDate, Set<Team> teams) {
        this.name = name;
        this.creationDate = creationDate;
        this.teams = teams;
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

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

}
