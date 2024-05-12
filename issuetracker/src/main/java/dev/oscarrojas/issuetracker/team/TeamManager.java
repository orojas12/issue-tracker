package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserDao;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamManager {
    
    private final TeamDao teamDao;
    private final UserDao userDao;

    public TeamManager(TeamDao teamDao, UserDao userDao) {
        this.teamDao = teamDao;
        this.userDao = userDao;
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

    public TeamDetails createTeam(CreateTeamRequest request) throws NotFoundException {
        Team team = new Team();
        team.setName(request.name());
        team.setDateCreated(Instant.now());
        team = teamDao.save(team);
        return new TeamDetails(team.getId(), team.getName(), new ArrayList<>(team.getMembers()));
    }

    public void deleteTeam(String teamId) throws NotFoundException {
        teamDao.deleteById(teamId);
    }

    public TeamDetails addUserToTeam(String username, String teamId) throws NotFoundException, DuplicateElementException {
        Optional<User> userOpt = userDao.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format("User '%s' not found", username));
        }

        Optional<Team> teamOpt = teamDao.findById(teamId);
        if (teamOpt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", teamId));
        }

        Team team = teamOpt.get();
        User user = userOpt.get();

        TeamMember teamMember = new TeamMember(user.getUsername());

        team.addMember(teamMember);

        team = teamDao.save(team);

        return team.getDetails();
    }

    public TeamDetails removeUserFromTeam(String username, String teamId) throws NotFoundException {
        Optional<Team> opt = teamDao.findById(teamId);
        
        if (opt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", teamId));
        }

        Team team = opt.get();
        team.removeMember(username);
        team = teamDao.save(team);
        return team.getDetails();
    }

}
