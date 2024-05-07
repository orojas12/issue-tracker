package dev.oscarrojas.issuetracker.config;

import dev.oscarrojas.issuetracker.team.TeamMemberModel;
import dev.oscarrojas.issuetracker.team.TeamModel;
import dev.oscarrojas.issuetracker.team.TeamRepository;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;
import dev.oscarrojas.issuetracker.util.RandomStringGenerator;
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

    private TeamModel teamWithUsers(String name, Collection<UserModel> users) {
        var team = new TeamModel(RandomStringGenerator.getRandomString(8), name, Instant.now(), new HashSet<>());
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
                userWithUsername("susan"),
                userWithUsername("tommy"),
                userWithUsername("alicia"),
                userWithUsername("alex")
        );
        users = userRepository.saveAll(users);
        TeamModel team1 = teamWithUsers("QA Team", List.of(users.get(0), users.get(1), users.get(2)));
        TeamModel team2 = teamWithUsers("Backend", List.of(users.get(3), users.get(4), users.get(5), users.get(6)));
        TeamModel team3 = teamWithUsers("Frontend", List.of(users.get(1), users.get(4), users.get(3)));
        teamRepository.saveAll(List.of(team1, team2, team3));
    }
}
