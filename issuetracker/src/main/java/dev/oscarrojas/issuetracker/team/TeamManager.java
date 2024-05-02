package dev.oscarrojas.issuetracker.team;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserManager;

@Service
public class TeamManager {
    
    private final TeamDao teamDao;
    private final UserManager userManager;

    public TeamManager(TeamDao teamDao, UserManager userManager) {
        this.teamDao = teamDao;
        this.userManager = userManager;
    }

    public void addUserToTeam(String username, String teamId) throws NotFoundException, DuplicateElementException {
        Optional<User> user = userManager.getUser(username);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User '%s' not found", username));
        }

        Optional<Team> teamOpt = teamDao.findById(teamId);

        if (teamOpt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", teamId));
        }

        Team team = teamOpt.get();
        team.addMember(user.get());
        teamDao.update(teamId, team);
    }

    public void removeUserFromTeam(String username, String teamId) throws NotFoundException {
        Optional<Team> opt = teamDao.findById(teamId);
        
        if (opt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", teamId));
        }

        Team team = opt.get();
        team.removeMember(username);
        teamDao.update(teamId, team);
    }

}
