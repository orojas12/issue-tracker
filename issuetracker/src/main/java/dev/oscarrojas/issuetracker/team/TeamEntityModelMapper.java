package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.user.UserEntityModelMapper;

import java.util.HashSet;

public class TeamEntityModelMapper {

    public static Team toEntity(TeamModel model) {
        Team team = new Team();
        team.setId(model.getId());
        team.setName(model.getName());
        team.setDateCreated(model.getDateCreated());
        team.setMembers(new HashSet<>(UserEntityModelMapper.getEntities(model.getMembers())));
        return team;
    }

    public static TeamModel toModel(Team team) {
        TeamModel model = new TeamModel();
        model.setId(team.getId());
        model.setName(team.getName());
        model.setDateCreated(team.getDateCreated());
        model.setMembers(new HashSet<>(UserEntityModelMapper.getModels(team.getMembers())));
        return model;
    }

}
