package dev.oscarrojas.issuetracker.team;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.UserEntityModelMapper;

@Component
public class TeamDaoJpa implements TeamDao {

    private final TeamRepository repository;

    public TeamDaoJpa(TeamRepository repository) {
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
        return TeamEntityModelMapper.getEntity(model);
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
        if (model.isPresent()) {
            return Optional.of(TeamEntityModelMapper.getEntity(model.get()));
        } else {
            return Optional.empty();
        }
    }

}
