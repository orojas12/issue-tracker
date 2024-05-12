package dev.oscarrojas.issuetracker.config;

import dev.oscarrojas.issuetracker.team.TeamMemberModel;
import dev.oscarrojas.issuetracker.team.TeamModel;
import dev.oscarrojas.issuetracker.team.TeamRepository;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;
import dev.oscarrojas.issuetracker.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${issuetracker.insert-fake-data}")
    boolean shouldInsertData;

    private UserModel user(String username, String firstName, String lastName) {
        return new UserModel(
                RandomStringGenerator.getRandomString(10),
                username,
                firstName,
                lastName,
                Instant.now()
        );
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
        if (!shouldInsertData) return;
        List<UserModel> users = List.of(
                user("orojas12", "Oscar", "Rojas"),
                user("ericg463", "Eric", "Gutierrez"),
                user("sarahparker06", "Sarah", "Parker"),
                user("charlestheman", "Charles", "Owens"),
                user("johnnyboy01", "Johnny", "Fasber"),
                user("lovelysusan1", "Susan", "Bearmont"),
                user("tommy123", "Tom", "Gates"),
                user("aleeshaa7", "Alicia", "Green"),
                user("benalex02", "Alexis", "Martinez")
        );
        users = userRepository.saveAll(users);
        TeamModel team1 = teamWithUsers("QA Team", List.of(users.get(0), users.get(1), users.get(2)));
        TeamModel team2 = teamWithUsers("Backend", List.of(users.get(3), users.get(4), users.get(5), users.get(6)));
        TeamModel team3 = teamWithUsers("Frontend", List.of(users.get(1), users.get(4), users.get(3)));
        teamRepository.saveAll(List.of(team1, team2, team3));
    }
}
