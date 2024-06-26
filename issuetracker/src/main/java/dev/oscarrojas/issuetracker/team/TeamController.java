package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {
    
    private final TeamManager teamManager;

    public TeamController(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @GetMapping
    public List<TeamDetails> getAllTeams() {
        return teamManager.getAllTeams();
    }

    @PostMapping
    public TeamDetails createTeam(@RequestBody CreateTeamRequest dto) {
        try {
            return teamManager.createTeam(dto);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{teamId}")
    public TeamDetails getTeamDetails(@PathVariable(name = "teamId") String teamId) {
        try {
            return teamManager.getTeam(teamId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{teamId}")
    public void deleteTeam(@PathVariable("teamId") String teamId) {
        try {
            teamManager.deleteTeam(teamId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/{teamId}/members")
    public TeamDetails addTeamMember(
            @PathVariable(name = "teamId") String teamId,
            @RequestParam(name = "username") String username
    ) {
        try {
            return teamManager.addUserToTeam(username, teamId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (DuplicateElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{teamId}/members")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTeamMember(
            @PathVariable(name = "teamId") String teamId,
            @RequestParam(name = "username") String username
    ) {
        try {
            teamManager.removeUserFromTeam(username, teamId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
