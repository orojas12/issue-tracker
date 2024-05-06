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
    void addUserToTeam_UsernameAndTeamId_AddsUserToTeam() throws NotFoundException, DuplicateElementException {
        User user = withUsername("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamDao.update(team.getId(), team)).thenReturn(team);
        when(userManager.getUser(user.getUsername())).thenReturn(Optional.of(user));

        TeamManager manager = new TeamManager(teamDao, userManager);
        List<TeamMember> teamMembers = manager.addUserToTeam(user.getUsername(), team.getId());

        assertTrue(team.hasMember(user.getUsername()));
        assertEquals(teamMembers.getFirst().username(), user.getUsername());
        verify(teamDao).update(team.getId(), team);
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
        verify(teamDao).update(team.getId(), team);
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
        public Team create(Team team) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public void delete(String id) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public Optional<Team> findById(String id) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public Team update(String id, Team team) {
            throw new RuntimeException("Unimplemented method");
        }

    }
}
