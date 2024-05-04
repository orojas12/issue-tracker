package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.RoleModel;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TeamDaoJpa.class)
public class TeamDaoJpaIT {
    
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TeamDaoJpa teamDao;

    private UserModel modelWithUsername(String username) {
        return new UserModel(username, "password", new HashSet<>(), Instant.now());
    }

    private User entityWithUsername(String username) {
        return new User(username, new HashSet<>(), Instant.now());
    }

    @Test
    void update_Team_updatesTeamModel() throws NotFoundException, DuplicateElementException {
        // insert test data
        var user1 = entityManager.persist(modelWithUsername("user1"));
        var user2 = entityManager.persist(modelWithUsername("user2"));
        var teamModel = entityManager.persist(new TeamModel(
                "id",
                "team1",
                Instant.now(),
                Set.of(user1, user2)
        ));

        // set up entities
        var team = new Team(
            teamModel.getId(),
            teamModel.getName(),
            teamModel.getDateCreated(),
            teamModel.getMembers().stream()
                    .map((model) -> new User(
                        model.getUsername(), 
                        model.getRoles().stream()
                                .map(RoleModel::getId)
                                .collect(Collectors.toSet()),
                        Instant.now()))
                    .collect(Collectors.toSet())
        );
        var newUser = entityManager.persist(modelWithUsername("user3"));
        var newUserEntity = entityWithUsername(newUser.getUsername());

        // mutate team and persist
        team.setName("team2");
        team.addMember(newUserEntity);
        team.removeMember("user1");
        teamDao.update(team.getId(), team);

        // query db
        teamModel = entityManager.find(TeamModel.class, team.getId());

        // assert mutations persisted
        assertEquals(team.getName(), teamModel.getName());
        assertEquals(2, teamModel.getMembers().size());
        for (UserModel model : teamModel.getMembers()) {
            boolean isUser2 = model.getUsername().equals("user2");
            boolean isUser3 = model.getUsername().equals("user3");
            assertTrue(isUser2 || isUser3);
        }

    }

}
