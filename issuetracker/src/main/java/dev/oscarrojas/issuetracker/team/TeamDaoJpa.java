package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.UserRepository;
import dev.oscarrojas.issuetracker.util.RandomStringGenerator;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TeamDaoJpa implements TeamDao {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamDaoJpa(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Team> findById(String id) {
        Optional<TeamModel> modelOpt = teamRepository.findById(id);
        return modelOpt.map(TeamDaoJpa::mapToEntity);
    }


    @Override
    public List<Team> findAll() {
        List<TeamModel> models = teamRepository.findAll();
        return models.stream().map(TeamDaoJpa::mapToEntity).toList();
    }

    @Override
    public Team save(Team team) throws NotFoundException {
        TeamModel teamModel;
        if (team.getId() == null) {
            teamModel = new TeamModel();
            // generate unique id
            String id = RandomStringGenerator.getRandomString(8);
            teamModel.setId(id);
        } else {
            Optional<TeamModel> opt = teamRepository.findById(team.getId());
            teamModel = opt.orElseThrow(() -> new RuntimeException(
                            String.format(
                                    "Could not find team id '%s'. If this is a new team" +
                                    " please set team id to null.",
                                    team.getId()
                            )
                    )
            );
        }

        teamModel.setName(team.getName());
        teamModel.setDateCreated(team.getDateCreated());

        List<String> usernames = team.getMembers().stream()
                .map((member) -> member.username())
                .toList();

        List<TeamMemberModel> teamMemberModels = userRepository.findAllByUsernameIn(usernames)
                .stream()
                .map((userModel -> new TeamMemberModel(
                            userModel,
                            teamModel
                    )
                ))
                .toList();

        if (teamMemberModels.size() != usernames.size()) {
            throw new NotFoundException(
                    String.format("One or more users in team '%s' do not exist", team.getId())
            );
        }

        teamModel.setMembers(teamMemberModels);

        TeamModel result = teamRepository.save(teamModel);

        return mapToEntity(result);
    }

    @Override
    public void deleteById(String id) throws NotFoundException {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("Team id '%s' not found", id));
        }
    }

    private static Team mapToEntity(TeamModel model) {
        Team entity = new Team();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDateCreated(model.getDateCreated());
        entity.setMembers(model.getMembers().stream()
                .map(member ->
                        new TeamMember(member.getUser().getUsername())
                )
                .collect(Collectors.toCollection(HashSet::new))
        );
        return entity;
    }

}
