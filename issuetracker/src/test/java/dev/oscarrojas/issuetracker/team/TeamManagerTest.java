package dev.oscarrojas.issuetracker.team;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserManager;

@ExtendWith(MockitoExtension.class)
public class TeamManagerTest {

    @Mock
    UserManager userManager;

    @Mock
    TestTeamDao teamDao;
    
    @Test
    void addUserToTeam_UsernameAndTeamId_AddsUserToTeam() throws NotFoundException, DuplicateElementException {
        User user = new User("username", new HashSet<>(), Instant.now());
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));
        when(userManager.getUser(user.getUsername())).thenReturn(Optional.of(user));

        TeamManager manager = new TeamManager(teamDao, userManager);
        manager.addUserToTeam(user.getUsername(), team.getId());

        assertTrue(team.hasMember(user.getUsername()));
        verify(teamDao).update(team.getId(), team);
    }

    @Test
    void addUserToTeam_UsernameAndTeamId_throwsNotFoundExceptionIfUserNotFound() {
        User user = new User("username", new HashSet<>(), Instant.now());
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
        User user = new User("username", new HashSet<>(), Instant.now());
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
        User user = new User("username", new HashSet<>(), Instant.now());
        Team team = new Team("id", "name", Instant.now(), new HashSet<>(Set.of(user)));

        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));

        TeamManager manager = new TeamManager(teamDao, userManager);
        manager.removeUserFromTeam(user.getUsername(), team.getId());

        assertFalse(team.hasMember(user.getUsername()));
        verify(teamDao).update(team.getId(), team);
    }

    @Test
    void removeUserFromTeam_UsernameAndTeamId_throwsNotFoundExceptionIfUserNotFound() {
        User user = new User("username", new HashSet<>(), Instant.now());
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
        User user = new User("username", new HashSet<>(), Instant.now());
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        when(teamDao.findById(team.getId())).thenReturn(Optional.empty());

        TeamManager manager = new TeamManager(teamDao, userManager);
        assertThrows(
            NotFoundException.class,
            () -> manager.removeUserFromTeam(user.getUsername(), team.getId())
        );
    }
    
    class TestTeamDao implements TeamDao {

        @Override
        public Team create(Team team) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public void delete(String id) throws NotFoundException {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public Optional<Team> findById(String id) {
            throw new RuntimeException("Unimplemented method");
        }

        @Override
        public Team update(String id, Team team) throws NotFoundException {
            throw new RuntimeException("Unimplemented method");
        }

    }
}
