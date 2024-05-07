package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TeamJpaDao implements TeamDao {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamJpaDao(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Team> findById(String id) {
        Optional<TeamModel> modelOpt = teamRepository.findById(id);
        return modelOpt.map(TeamJpaDao::mapToEntity);
    }


    @Override
    public List<Team> findAll() {
        List<TeamModel> models = teamRepository.findAll();
        return models.stream().map(TeamJpaDao::mapToEntity).toList();
    }

    @Override
    public Team save(Team team) throws NotFoundException {
        Optional<TeamModel> opt = teamRepository.findById(team.getId());
        TeamModel model = opt.orElseGet(TeamModel::new);

        if (team.getId() == null) {
            // generate unique id
        } else {
            model.setId(team.getId());
        }

        model.setName(team.getName());
        model.setDateCreated(team.getDateCreated());

        List<String> usernames = new ArrayList<>(team.getMembers().size());

        Set<TeamMemberModel> teamMembers = team.getMembers().stream().map((member) -> {
            usernames.add(member.username());
            UserModel user = userRepository.getReferenceById(member.username());
            return new TeamMemberModel(user, model);
        }).collect(Collectors.toSet());

        // verify all users exist before saving them to model
        if (userRepository.countByUsername(usernames) < usernames.size()) {
            throw new NotFoundException(
                    String.format("One or more users added to team '%s' do not exist", team.getId())
            );
        }

        model.setMembers(teamMembers);

        TeamModel result = teamRepository.save(model);

        return mapToEntity(result);
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    private static Team mapToEntity(TeamModel model) {
        Team entity = new Team();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDateCreated(model.getDateCreated());
        entity.setMembers(model.getMembers().stream()
                .map(member ->
                        new TeamMember(member.getUser().getUsername(), member.getTeam().getId())
                )
                .collect(Collectors.toCollection(HashSet::new))
        );
        return entity;
    }

}
