package dev.oscarrojas.issuetracker.team;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class TeamDaoImpl implements TeamDao {

    private final TeamRepository repository;

    public TeamDaoImpl(TeamRepository repository) {
        this.repository = repository;
    }

    @Override
    public Team update(Team team) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Team create(Team team) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Optional<Team> findById(String id) {
        Optional<TeamEntity> entity = repository.findById(id);
        if (entity.isPresent()) {
            return Optional.of(entityToTeam(entity.get()));
        } else {
            return Optional.empty();
        }
    }

    Team entityToTeam(TeamEntity entity) {
        Team team = new Team();
        team.setId(entity.getId());
        team.setName(entity.getName());
        team.setCreationDate(entity.getCreationDate());
        return team;
    }

}
