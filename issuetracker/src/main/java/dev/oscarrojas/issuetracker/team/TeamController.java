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

    @GetMapping("/{teamId}")
    public TeamDetails getTeamDetails(@PathVariable(name = "teamId") String teamId) {
        try {
            return teamManager.getTeam(teamId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/{teamId}/members")
    public List<TeamMember> addTeamMember(
            @PathVariable(name = "teamId") String teamId, @RequestParam(name = "username") String username) {
        try {
            return teamManager.addUserToTeam(username, teamId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (DuplicateElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
