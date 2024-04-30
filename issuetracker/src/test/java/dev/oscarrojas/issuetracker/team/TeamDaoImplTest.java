package dev.oscarrojas.issuetracker.team;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class TeamDaoImplTest {

    @MockBean
    TeamRepository repository;

    @Test
    void findById_Id_returnsTeam() {
        TeamDaoImpl dao = new TeamDaoImpl(repository);
        TeamEntity entity = new TeamEntity();
        entity.setId("id");
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Optional<Team> opt = dao.findById(entity.getId());
        assertTrue(opt.isPresent());
        assertEquals(entity.getId(), opt.get().getId());
    }

    @Test
    void findById_Id_returnsEmptyIfNotFound() {
        TeamDaoImpl dao = new TeamDaoImpl(repository);
        String id = "id";
        when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<Team> opt = dao.findById(id);
        assertTrue(opt.isEmpty());
    }

    @Test
    void entityToTeam_Entity_CreatesTeamWithEntityData() {
        TeamDaoImpl dao = new TeamDaoImpl(repository);
        TeamEntity entity = new TeamEntity("id", "name", Instant.now());
        Team team = dao.entityToTeam(entity);
        assertEquals(entity.getId(), team.getId());
        assertEquals(entity.getName(), team.getName());
        assertEquals(entity.getCreationDate(), team.getCreationDate());
    }

}
