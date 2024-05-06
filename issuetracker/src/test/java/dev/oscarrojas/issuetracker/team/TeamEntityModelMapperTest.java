package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserModel;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeamEntityModelMapperTest {

    private UserModel modelWithUsername(String username) {
        return new UserModel(username, "password", Instant.now());
    }

    private User entityWithUsername(String username) {
        return new User(username, Instant.now());
    }

    @Test
    void toEntity_Model_returnsEntityWithSameData() {
        TeamModel model = new TeamModel();
        model.setId("id");
        model.setName("name");
        model.setMembers(new HashSet<>());
        model.setDateCreated(Instant.now());

        Team entity = TeamEntityModelMapper.toEntity(model);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDateCreated(), model.getDateCreated());
        assertEquals(entity.getMembers().size(), 0);
    }

    @Test
    void toEntity_Model_mapsInnerModels() {
        TeamModel teamModel = new TeamModel();
        UserModel userModel = new UserModel("username", "password", Instant.now());
        TeamMemberModel teamMemberModel = new TeamMemberModel(userModel, teamModel);
        teamModel.setId("id");
        teamModel.setName("name");
        teamModel.setMembers(Set.of(teamMemberModel));
        teamModel.setDateCreated(Instant.now());

        Team entity = TeamEntityModelMapper.toEntity(teamModel);

        // assert team member is in the set
        for (TeamMember member : entity.getMembers()) {
            assertEquals(teamMemberModel.getUser().getUsername(), member.username());
        }
    }

    @Test
    void toModel_Entity_returnsModelWithSameData() {
        Team entity = new Team();
        entity.setId("id");
        entity.setName("name");
        entity.setMembers(new HashSet<>());
        entity.setDateCreated(Instant.now());

        TeamModel model = TeamEntityModelMapper.toModel(entity);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getDateCreated(), entity.getDateCreated());
        assertEquals(model.getMembers().size(), 0);
    }

    @Test
    void toModel_Entity_mapsInnerEntities() {
        Team entity = new Team();
        TeamMember teamMember = new TeamMember("user1", "team1");
        entity.setId("team1");
        entity.setName("name");
        entity.setMembers(new HashSet<>(Set.of(teamMember)));
        entity.setDateCreated(Instant.now());

        TeamModel model = TeamEntityModelMapper.toModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDateCreated(), model.getDateCreated());
        assertEquals(1, model.getMembers().size());

        // assert team member is in the set
        for (TeamMemberModel member : model.getMembers()) {
            assertEquals(teamMember.username(), member.getUser().getUsername());
        }
    }

}
