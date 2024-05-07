package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamManager {
    
    private final TeamDao teamDao;
    private final UserManager userManager;

    public TeamManager(TeamDao teamDao, UserManager userManager) {
        this.teamDao = teamDao;
        this.userManager = userManager;
    }

    public TeamDetails getTeam(String teamId) throws NotFoundException {
        Optional<Team> teamOpt = teamDao.findById(teamId);

        if (teamOpt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", teamId));
        }

        Team team = teamOpt.get();

        return new TeamDetails(
                team.getId(),
                team.getName(),
                new ArrayList<>(team.getMembers())
        );
    }

    public List<TeamDetails> getAllTeams() {
        return teamDao.findAll().stream()
                .map((team) ->
                        new TeamDetails(
                                team.getId(),
                                team.getName(),
                                new ArrayList<>(team.getMembers()))
                )
                .toList();
    }

    public List<TeamMember> addUserToTeam(String username, String teamId) throws NotFoundException, DuplicateElementException {
        Optional<User> userOpt = userManager.getUser(username);

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format("User '%s' not found", username));
        }

        Optional<Team> teamOpt = teamDao.findById(teamId);

        if (teamOpt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", teamId));
        }

        Team team = teamOpt.get();
        User user = userOpt.get();
        TeamMember teamMember = new TeamMember(
                user.getUsername(),
                team.getId()
        );
        team.addMember(teamMember);
        team = teamDao.save(team);
        return new ArrayList<>(team.getMembers());
    }

    public void removeUserFromTeam(String username, String teamId) throws NotFoundException {
        Optional<Team> opt = teamDao.findById(teamId);
        
        if (opt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", teamId));
        }

        Team team = opt.get();
        team.removeMember(username);
        teamDao.save(team);
    }

}
