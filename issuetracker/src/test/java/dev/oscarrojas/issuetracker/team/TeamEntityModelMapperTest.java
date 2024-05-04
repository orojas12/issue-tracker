package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserModel;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeamEntityModelMapperTest {

    private UserModel modelWithUsername(String username) {
        return new UserModel(username, "password", new HashSet<>(), Instant.now());
    }

    private User entityWithUsername(String username) {
        return new User(username, new HashSet<>(), Instant.now());
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
        TeamModel model = new TeamModel();
        model.setId("id");
        model.setName("name");
        model.setMembers(Set.of(modelWithUsername("user1"), modelWithUsername("user2")));
        model.setDateCreated(Instant.now());

        Team entity = TeamEntityModelMapper.toEntity(model);

        // assert user1 and user2 are in the set
        boolean hasUser1 = false;
        boolean hasUser2 = false;
        for (User user : entity.getMembers()) {
            if (user.getUsername().equals("user1")) {
                hasUser1 = true;
            } else if (user.getUsername().equals("user2")) {
                hasUser2 = true;
            }
        }
        assertTrue(hasUser1 && hasUser2);
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
        entity.setId("id");
        entity.setName("name");
        entity.setMembers(Set.of(entityWithUsername("user1"), entityWithUsername("user2")));
        entity.setDateCreated(Instant.now());

        TeamModel model = TeamEntityModelMapper.toModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDateCreated(), model.getDateCreated());
        assertEquals(2, model.getMembers().size());

        // assert user1 and user2 are in the set
        boolean hasUser1 = false;
        boolean hasUser2 = false;
        for (UserModel user : model.getMembers()) {
            if (user.getUsername().equals("user1")) {
                hasUser1 = true;
            } else if (user.getUsername().equals("user2")) {
                hasUser2 = true;
            }
        }
        assertTrue(hasUser1 && hasUser2);
    }

}
