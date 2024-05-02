package dev.oscarrojas.issuetracker.team;

import java.util.HashSet;

import dev.oscarrojas.issuetracker.user.UserEntityModelMapper;

public class TeamEntityModelMapper {

    public static Team getEntity(TeamModel model) {
        Team team = new Team();
        team.setId(model.getId());
        team.setName(model.getName());
        team.setDateCreated(model.getDateCreated());
        team.setMembers(new HashSet<>(UserEntityModelMapper.getEntities(model.getMembers())));
        return team;
    }

    public static TeamModel getModel(Team team) {
        TeamModel model = new TeamModel();
        model.setId(team.getId());
        model.setName(team.getName());
        model.setDateCreated(team.getDateCreated());
        model.setMembers(new HashSet<>(UserEntityModelMapper.getModels(team.getMembers())));
        return model;
    }

}
