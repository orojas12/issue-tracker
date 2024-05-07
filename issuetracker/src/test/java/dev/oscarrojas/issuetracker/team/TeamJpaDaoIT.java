package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.IntegrationTestConfig;
import dev.oscarrojas.issuetracker.IssueTracker;
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
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.oscarrojas.issuetracker.TestUtils.userModelWithUsername;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {IssueTracker.class, IntegrationTestConfig.class})
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

    @Test
    void findAll_returnsAllTeam() {
        var model1 = new TeamModel("team1", "Team 1", Instant.now(),
                new HashSet<>());
        var model2 = new TeamModel("team2", "Team 2", Instant.now(),
                new HashSet<>());
        var user1 = userModelWithUsername("user1");
        var user2 = userModelWithUsername("user2");
        user1 = entityManager.persistFlushFind(user1);
        user2 = entityManager.persistFlushFind(user2);
        model1.getMembers().add(new TeamMemberModel(user1, model1));
        model2.getMembers().add(new TeamMemberModel(user2, model2));

        model1 = entityManager.persistFlushFind(model1);
        model2 = entityManager.persistFlushFind(model2);

        List<Team> results = teamDao.findAll();

        assertEquals(2, results.size());

        Team result1 = results.get(0);
        assertEquals(model1.getId(), result1.getId());
        assertEquals(model1.getName(), result1.getName());
        assertEquals(model1.getDateCreated(), result1.getDateCreated());
        assertEquals(1, result1.getMembers().size());
        assertTrue(result1.hasMember(user1.getUsername()));

        Team result2 = results.get(1);
        assertEquals(model2.getId(), result2.getId());
        assertEquals(model2.getName(), result2.getName());
        assertEquals(model2.getDateCreated(), result2.getDateCreated());
        assertEquals(1, result2.getMembers().size());
        assertTrue(result2.hasMember(user2.getUsername()));
    }

    @Test
    void save_Team_createsNewTeamModel() throws NotFoundException {
        // setup
        var user1 = entityManager.persist(modelWithUsername("user1"));
        var team = new Team(
                null,
                "name",
                Instant.now(),
                new HashSet<>(Set.of(new TeamMember(user1.getUsername())))
        );

        // action
        team = teamDao.save(team);

        // new team data should be saved to db
        var result = entityManager.find(TeamModel.class, team.getId());
        assertEquals(team.getName(), result.getName());
        assertEquals(team.getDateCreated(), result.getDateCreated());
        assertTrue(team.hasMember(user1.getUsername()));
        assertEquals(1, result.getMembers().size());
    }

    @Test
    void save_Team_updatesTeamModel() throws NotFoundException, DuplicateElementException {
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
                        model.getUser().getUsername()
                    ))
                    .collect(Collectors.toCollection(HashSet::new))
        );
        var newUser = entityManager.persist(modelWithUsername("user3"));
        var newTeamMember = new TeamMember(newUser.getUsername());

        // mutate team and persist
        team.setName("team2");
        team.addMember(newTeamMember);
        team.removeMember("user1");
        teamDao.save(team);

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

    @Test
    void deleteById_TeamId_deletesTeamFromDatabase() throws NotFoundException {
        TeamModel model = entityManager.persistFlushFind(
                new TeamModel("team1", "QA Team", Instant.now(), new HashSet<>())
        );

        teamDao.deleteById(model.getId());

        TeamModel result = entityManager.find(TeamModel.class, model.getId());
        assertNull(result);
    }

    @Test
    void deleteById_TeamId_throwsNotFoundIfTeamNotFoundInDatabase() {
        assertThrows(NotFoundException.class, () -> teamDao.deleteById("team1"));
    }

}
