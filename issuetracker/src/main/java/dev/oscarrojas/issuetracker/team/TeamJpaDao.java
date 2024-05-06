package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TeamJpaDao implements TeamDao {

    private final TeamRepository teamRepository;
    private final UserRepository userRespository;

    public TeamJpaDao(TeamRepository teamRepository, UserRepository userRespository) {
        this.teamRepository = teamRepository;
        this.userRespository = userRespository;
    }

    @Override
    public Team update(String teamId, Team team) throws NotFoundException {
        Optional<TeamModel> opt = teamRepository.findById(team.getId());

        if (opt.isEmpty()) {
            throw new NotFoundException(String.format("Team '%s' not found", team.getId()));
        }

        TeamModel teamModel = opt.get();
        teamModel.setName(team.getName());

        Set<TeamMemberModel> memberModels = team.getMembers().stream()
                .map((member) -> {
                    UserModel user = userRespository.getReferenceById(member.username());
                    return new TeamMemberModel(user, teamModel);
                })
                .collect(Collectors.toSet());

        teamModel.setMembers(memberModels);

        TeamModel result = teamRepository.save(teamModel);

        return TeamEntityModelMapper.toEntity(result);
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
        Optional<TeamModel> model = teamRepository.findById(id);
        return model.map(TeamEntityModelMapper::toEntity);
    }

}
