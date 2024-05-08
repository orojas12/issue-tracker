package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;
import dev.oscarrojas.issuetracker.util.RandomStringGenerator;
import org.springframework.stereotype.Component;

import java.util.*;
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
        TeamModel model;
        if (team.getId() == null) {
            model = new TeamModel();
            // generate unique id
            String id = RandomStringGenerator.getRandomString(8);
            model.setId(id);
        } else {
            Optional<TeamModel> opt = teamRepository.findById(team.getId());
            model = opt.orElseThrow(() -> new RuntimeException(
                            String.format(
                                    "Could not find team id '%s'. If this is a new team" +
                                    " please set team id to null.",
                                    team.getId()
                            )
                    )
            );
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
