package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.UserEntityModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
public class TeamJpaDao implements TeamDao {

    private final TeamRepository repository;

    public TeamJpaDao(TeamRepository repository) {
        this.repository = repository;
    }

    @Override
    public Team update(String teamId, Team team) throws NotFoundException {
        Optional<TeamModel> opt = repository.findById(team.getId());

        if (opt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", team.getId()));
        }

        TeamModel model = opt.get();
        model.setName(team.getName());
        model.setMembers(new HashSet<>(UserEntityModelMapper.getModels(team.getMembers())));
        model = repository.save(model);
        return TeamEntityModelMapper.toEntity(model);
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
        Optional<TeamModel> model = repository.findById(id);
        return model.map(TeamEntityModelMapper::toEntity);
    }

}
