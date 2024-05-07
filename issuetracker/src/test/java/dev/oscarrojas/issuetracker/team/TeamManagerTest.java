package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamManagerTest {

    @Mock
    UserManager userManager;

    @Mock
    TestTeamDao teamDao;

    private User withUsername(String username) {
        return new User(username, Instant.now());
    }

    @Test
    void getAllTeams() {
        List<Team> teams = List.of(
                new Team("team1", "Team 1", Instant.now(),
                        new HashSet<>(Set.of(new TeamMember("user1", "team1")))),
                new Team("team2", "Team 2", Instant.now(),
                        new HashSet<>(Set.of(new TeamMember("user2", "team2"))))
        );
        when(teamDao.findAll()).thenReturn(teams);
        var teamManager = new TeamManager(teamDao, userManager);
        List<TeamDetails> results = teamManager.getAllTeams();
        var team1 = teams.get(0);
        var team2 = teams.get(1);

        var result1 = results.get(0);
        assertEquals(team1.getId(), result1.id());
        assertEquals(team1.getName(), result1.name());
        assertEquals(1, result1.teamMembers().size());
        assertEquals("user1", result1.teamMembers().getFirst().username());
        assertEquals("team1", result1.teamMembers().getFirst().teamId());

        var result2 = results.get(1);
        assertEquals(team2.getId(), result2.id());
        assertEquals(team2.getName(), result2.name());
        assertEquals(1, result2.teamMembers().size());
        assertEquals("user2", result2.teamMembers().getFirst().username());
        assertEquals("team2", result2.teamMembers().getFirst().teamId());
    }
    
    @Test
    void addUserToTeam_UsernameAndTeamId_AddsUserToTeam() throws NotFoundException, DuplicateElementException {
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamDao.save(team)).thenReturn(team);
        when(userManager.getUser(user.getUsername())).thenReturn(Optional.of(user));

        TeamManager manager = new TeamManager(teamDao, userManager);
        List<TeamMember> teamMembers = manager.addUserToTeam(user.getUsername(), team.getId());

        assertTrue(team.hasMember(user.getUsername()));
        assertEquals(teamMembers.getFirst().username(), user.getUsername());
        verify(teamDao).save(team);
    }

    @Test
    void addUserToTeam_UsernameAndTeamId_throwsNotFoundExceptionIfUserNotFound() {
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(userManager.getUser(user.getUsername())).thenReturn(Optional.empty());

        TeamManager manager = new TeamManager(teamDao, userManager);

        assertThrows(
            NotFoundException.class, 
            () -> manager.addUserToTeam(user.getUsername(), team.getId())
        );
    }

    @Test
    void addUserToTeam_UsernameAndTeamId_throwsNotFoundExceptionIfTeamNotFound() {
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(userManager.getUser(user.getUsername())).thenReturn(Optional.of(user));
        when(teamDao.findById(team.getId())).thenReturn(Optional.empty());

        TeamManager manager = new TeamManager(teamDao, userManager);

        assertThrows(
            NotFoundException.class, 
            () -> manager.addUserToTeam(user.getUsername(), team.getId())
        );
    }

    @Test
    void removeUserFromTeam_UsernameAndTeamId_removesUserFromTeam() throws NotFoundException {
        TeamMember teamMember = new TeamMember("user1", "team1");
        Team team = new Team("team1", "name", Instant.now(), new HashSet<>(Set.of(teamMember)));

        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));

        TeamManager manager = new TeamManager(teamDao, userManager);
        manager.removeUserFromTeam(teamMember.username(), team.getId());

        assertFalse(team.hasMember(teamMember.username()));
        verify(teamDao).save(team);
    }

    @Test
    void removeUserFromTeam_UsernameAndTeamId_throwsNotFoundExceptionIfUserNotFound() {
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));

        TeamManager manager = new TeamManager(teamDao, userManager);
        assertThrows(
            NotFoundException.class,
            () -> manager.removeUserFromTeam(user.getUsername(), team.getId())
        );
    }
    
    @Test
    void removeUserFromTeam_UsernameAndTeamId_throwsNotFoundExceptionIfTeamNotFound() {
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(teamDao.findById(team.getId())).thenReturn(Optional.empty());

        TeamManager manager = new TeamManager(teamDao, userManager);
        assertThrows(
            NotFoundException.class,
            () -> manager.removeUserFromTeam(user.getUsername(), team.getId())
        );
    }
    
    static class TestTeamDao implements TeamDao {


        @Override
        public void delete(String id) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public List<Team> findAll() {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public Team save(Team team) throws NotFoundException {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public Optional<Team> findById(String id) {
            throw new RuntimeException("Unimplemented method");
        }

    }
}
