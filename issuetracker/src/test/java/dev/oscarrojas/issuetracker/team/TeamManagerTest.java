package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamManagerTest {

    @Mock
    TestUserDao userDao;

    @Mock
    TestTeamDao teamDao;

    private User withUsername(String username) {
        return new User(username, Instant.now());
    }

    @Test
    void getAllTeams_returnsAllTeams() {
        // setup
        var team1 = new Team("team1", "Team 1", Instant.now(),
                        new HashSet<>(Set.of(new TeamMember("user1"))));
        var team2 = new Team("team2", "Team 2", Instant.now(),
                        new HashSet<>(Set.of(new TeamMember("user2"))));
        when(teamDao.findAll()).thenReturn(List.of(team1, team2));
        var teamManager = new TeamManager(teamDao, userDao);

        // action
        List<TeamDetails> results = teamManager.getAllTeams();

        // result1 == team1
        var result1 = results.get(0);
        assertEquals(team1.getId(), result1.id());
        assertEquals(team1.getName(), result1.name());
        assertEquals(1, result1.teamMembers().size());
        assertEquals("user1", result1.teamMembers().getFirst().username());

        // result2 == team2
        var result2 = results.get(1);
        assertEquals(team2.getId(), result2.id());
        assertEquals(team2.getName(), result2.name());
        assertEquals(1, result2.teamMembers().size());
        assertEquals("user2", result2.teamMembers().getFirst().username());
    }

    @Test
    void createTeam_CreateTeamRequest_createsAndReturnsTeam() throws NotFoundException {
        var request = new CreateTeamRequest("team1");
        ArgumentCaptor<Team> captor = ArgumentCaptor.forClass(Team.class);

        // return team object that was passed to save() method
        when(teamDao.save(argThat(arg -> {
            return arg.getName().equals(request.name());
        }))).thenAnswer(i -> {
            // simulate auto generated id
            var entity = (Team) i.getArguments()[0];
            entity.setId("team1");
            return entity;
        });

        TeamManager teamManager = new TeamManager(teamDao, userDao);
        TeamDetails result = teamManager.createTeam(request);
        assertNotNull(result.id());
        assertEquals(request.name(), result.name());
        assertNotNull(result.teamMembers());
    }

    @Test
    void deleteTeam_TeamId_callsDeleteMethodOnTeamDao() {
        String teamId = "team1";
        doNothing().when(teamDao).deleteById(eq(teamId));
        teamDao.deleteById(teamId);
    }
    
    @Test
    void addUserToTeam_UsernameAndTeamId_AddsUserToTeamAndSaves() throws NotFoundException, DuplicateElementException {
        // setup
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());
        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamDao.save(team)).thenReturn(team);
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        TeamManager manager = new TeamManager(teamDao, userDao);

        // action
        manager.addUserToTeam(user.getUsername(), team.getId());

        assertTrue(team.hasMember(user.getUsername()));
        verify(teamDao).save(argThat(arg -> arg.hasMember(user.getUsername())));
    }

    @Test
    void addUserToTeam_UsernameAndTeamId_throwsNotFoundExceptionIfUserNotFound() {
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        TeamManager manager = new TeamManager(teamDao, userDao);

        assertThrows(
            NotFoundException.class, 
            () -> manager.addUserToTeam(user.getUsername(), team.getId())
        );
    }

    @Test
    void addUserToTeam_UsernameAndTeamId_throwsNotFoundExceptionIfTeamNotFound() {
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(teamDao.findById(team.getId())).thenReturn(Optional.empty());

        TeamManager manager = new TeamManager(teamDao, userDao);

        assertThrows(
            NotFoundException.class, 
            () -> manager.addUserToTeam(user.getUsername(), team.getId())
        );
    }

    @Test
    void removeUserFromTeam_UsernameAndTeamId_removesUserFromTeam() throws NotFoundException {
        TeamMember teamMember = new TeamMember("user1");
        Team team = new Team("team1", "name", Instant.now(), new HashSet<>(Set.of(teamMember)));

        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamDao.save(argThat(arg -> arg.getId().equals(team.getId()))))
                .thenAnswer(i -> i.getArguments()[0]);

        TeamManager manager = new TeamManager(teamDao, userDao);
        manager.removeUserFromTeam(teamMember.username(), team.getId());

        assertFalse(team.hasMember(teamMember.username()));
        verify(teamDao).save(team);
    }

    @Test
    void removeUserFromTeam_UsernameAndTeamId_throwsNotFoundExceptionIfUserNotFound() {
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));

        TeamManager manager = new TeamManager(teamDao, userDao);
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

        TeamManager manager = new TeamManager(teamDao, userDao);
        assertThrows(
            NotFoundException.class,
            () -> manager.removeUserFromTeam(user.getUsername(), team.getId())
        );
    }
    
    static class TestTeamDao implements TeamDao {


        @Override
        public void deleteById(String id) {
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

    static class TestUserDao implements UserDao {

        @Override
        public Optional<User> findByUsername(String username) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public List<User> findAll() {
            throw new RuntimeException("Unimplemented method");
        }
    }
}
