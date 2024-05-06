package dev.oscarrojas.issuetracker.config;

import dev.oscarrojas.issuetracker.team.TeamMemberModel;
import dev.oscarrojas.issuetracker.team.TeamModel;
import dev.oscarrojas.issuetracker.team.TeamRepository;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
public class InitialDataLoader implements ApplicationRunner {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    private UserModel userWithUsername(String username) {
        return new UserModel(username, "password", Instant.now());
    }

    private TeamModel teamWithUsers(Collection<UserModel> users) {
        var team = new TeamModel("team1", "QA Team", Instant.now(), new HashSet<>());
        for (UserModel user : users) {
            team.getMembers().add(new TeamMemberModel(user, team));
        }
        return team;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<UserModel> users = List.of(
                userWithUsername("oscar"),
                userWithUsername("eric"),
                userWithUsername("sarah"),
                userWithUsername("charles"),
                userWithUsername("john"),
                userWithUsername("susan")
        );
        users = userRepository.saveAll(users);
        TeamModel team = teamWithUsers(List.of(users.get(0), users.get(1), users.get(2)));
        teamRepository.save(team);
    }
}
