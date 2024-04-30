package dev.oscarrojas.issuetracker.team;

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

}
