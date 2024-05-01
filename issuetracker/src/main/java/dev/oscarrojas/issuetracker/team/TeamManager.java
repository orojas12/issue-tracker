package dev.oscarrojas.issuetracker.team;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserDao;

@Service
public class TeamManager {
    
    private final TeamDao teamDao;
    private final UserDao userDao;

    public TeamManager(TeamDao teamDao, UserDao userDao) {
        this.teamDao = teamDao;
        this.userDao = userDao;
    }

    public void addUserToTeam(String username, String teamId) throws NotFoundException, DuplicateElementException {
        Optional<User> userOpt = userDao.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format("User '%s' not found", username));
        }

        Optional<Team> teamOpt = teamDao.findById(teamId);

        if (teamOpt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", teamId));
        }

        User user = userOpt.get();
        Team team = teamOpt.get();
        team.addMember(user);
        teamDao.update(team);
    }

    public void removeUserFromTeam(String username, String teamId) throws NotFoundException {
        Optional<Team> teamOpt = teamDao.findById(teamId);
        
        if (teamOpt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", teamId));
        }

        Team team = teamOpt.get();
        team.removeMember(username);
        teamDao.update(team);
    }

}
