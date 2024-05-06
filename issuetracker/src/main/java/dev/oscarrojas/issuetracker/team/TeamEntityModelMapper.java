package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.user.UserModel;

import java.util.HashSet;

import static java.util.stream.Collectors.toCollection;

public class TeamEntityModelMapper {

    public static Team toEntity(TeamModel model) {
        Team team = new Team();
        team.setId(model.getId());
        team.setName(model.getName());
        team.setDateCreated(model.getDateCreated());
        team.setMembers(model.getMembers().stream()
                .map(TeamMemberEntityModelMapper::toEntity)
                .collect(toCollection(HashSet::new)));
        return team;
    }

    public static TeamModel toModel(Team team) {
        TeamModel teamModel = new TeamModel();
        teamModel.setId(team.getId());
        teamModel.setName(team.getName());
        teamModel.setDateCreated(team.getDateCreated());
        teamModel.setMembers(team.getMembers().stream()
                .map((member) -> {
                    UserModel userModel = new UserModel();
                    userModel.setUsername(member.username());
                    return new TeamMemberModel(userModel);
                })
                .collect(toCollection(HashSet::new))
        );
        return teamModel;
    }

    public static class TeamMemberEntityModelMapper {

        public static TeamMember toEntity(TeamMemberModel model) {
            return new TeamMember(
                    model.getUser().getUsername(),
                    model.getTeam().getId()
            );
        }

    }

}
