package dev.oscarrojas.issuetracker.team;

import org.springframework.stereotype.Service;

@Service
public class TeamManager {
    
    private final TeamDao teamDao;

    public TeamManager(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    public void addUserToTeam(String username, String teamId) {

    }

    public void removeUserFromTeam(String username, String teamId) {

    }

}
