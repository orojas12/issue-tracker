package dev.oscarrojas.issuetracker.team;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserModel;

@DataJpaTest
@Import(TeamDaoJpa.class)
public class TeamDaoJpaIT {
    
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TeamDaoJpa teamDao;

    @Test
    void update_User_updatesUser() {
        var userModel = new UserModel("user1", "password", new HashSet<>(), Instant.now());
        var teamModel = new TeamModel("id", "name", Instant.now(), Set.of(userModel));
        teamModel = entityManager.persistFlushFind(teamModel);
            
        var team = new Team(
            teamModel.getId(),
            teamModel.getName(),
            teamModel.getDateCreated(),
            teamModel.getMembers().stream()
                    .map((model) -> new User(
                        model.getUsername(), 
                        model.getRoles().stream()
                                .map((role) -> role.getId())
                                .collect(Collectors.toSet()),
                        Instant.now()))
                    .collect(Collectors.toSet())
        );

        team.setName("team1");

        
    }
    
}
