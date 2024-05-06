package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TeamJpaDao.class)
public class TeamJpaDaoIT {
    
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TeamJpaDao teamDao;

    private UserModel modelWithUsername(String username) {
        return new UserModel(username, "password", Instant.now());
    }

    private User entityWithUsername(String username) {
        return new User(username, Instant.now());
    }

    // TODO: sqlite not generating sequences
    @Test
    void update_Team_updatesTeamModel() throws NotFoundException, DuplicateElementException {
        // insert test data
        var user1 = entityManager.persist(modelWithUsername("user1"));
        var user2 = entityManager.persist(modelWithUsername("user2"));
        var teamModel = entityManager.persist(new TeamModel(
                "id",
                "team1",
                Instant.now(),
                new HashSet<>()
        ));
        var teamMember1 = new TeamMemberModel(user1, teamModel);
        var teamMember2 = new TeamMemberModel(user2, teamModel);
        teamModel.setMembers(new HashSet<>(Set.of(teamMember1, teamMember2)));
        teamModel = entityManager.merge(teamModel);

        // set up entities
        var team = new Team(
            teamModel.getId(),
            teamModel.getName(),
            teamModel.getDateCreated(),
            teamModel.getMembers().stream()
                    .map((model) -> new TeamMember(
                        model.getUser().getUsername(), model.getTeam().getId()
                    ))
                    .collect(Collectors.toCollection(HashSet::new))
        );
        var newUser = entityManager.persist(modelWithUsername("user3"));
        var newTeamMember = new TeamMember(newUser.getUsername(), team.getId());

        // mutate team and persist
        team.setName("team2");
        team.addMember(newTeamMember);
        team.removeMember("user1");
        teamDao.update(team.getId(), team);

        // query db
        teamModel = entityManager.find(TeamModel.class, team.getId());

        // assert mutations persisted
        assertEquals(team.getName(), teamModel.getName());
        assertEquals(2, teamModel.getMembers().size());
        boolean hasUser1 = false;
        for (TeamMemberModel model : teamModel.getMembers()) {
            if (model.getUser().getUsername().equals("user1")) {
                hasUser1 = true;
                break;
            }
        }
        assertFalse(hasUser1);

    }

}
