package dev.oscarrojas.issuetracker.team;

import org.springframework.stereotype.Controller;

@Controller
public class TeamController {
    
    private final TeamManager manager;

    public TeamController(TeamManager manager) {
        this.manager = manager;
    }


}
