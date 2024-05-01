package dev.oscarrojas.issuetracker.team;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserDao;
import dev.oscarrojas.issuetracker.user.UserDaoImpl;

public class TeamManagerTest {
    
    @Test
    void addUserToTeam_UsernameAndTeamId_AddsUserToTeam() throws NotFoundException, DuplicateElementException {
        User user = new User("username", new HashSet<>(), Instant.now());
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());
        UserDao userDao = mock(UserDaoImpl.class);
        TeamDao teamDao = mock(TeamDaoImpl.class);

        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));

        TeamManager manager = new TeamManager(teamDao, userDao);
        manager.addUserToTeam(user.getUsername(), team.getId());

        assertTrue(team.hasMember(user.getUsername()));
        verify(teamDao).update(team);
    }

    @Test
    void addUserToTeam_UsernameAndTeamId_throwsNotFoundExceptionIfUserNotFound() {
        User user = new User("username", new HashSet<>(), Instant.now());
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());
        UserDao userDao = mock(UserDaoImpl.class);
        TeamDao teamDao = mock(TeamDaoImpl.class);

        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));

        TeamManager manager = new TeamManager(teamDao, userDao);

        assertThrows(
            NotFoundException.class, 
            () -> manager.addUserToTeam(user.getUsername(), team.getId())
        );
    }

    @Test
    void addUserToTeam_UsernameAndTeamId_throwsNotFoundExceptionIfTeamNotFound() {
        User user = new User("username", new HashSet<>(), Instant.now());
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());
        UserDao userDao = mock(UserDaoImpl.class);
        TeamDao teamDao = mock(TeamDaoImpl.class);

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
        User user = new User("username", new HashSet<>(), Instant.now());
        Team team = new Team("id", "name", Instant.now(), new HashSet<>(Set.of(user)));
        UserDao userDao = mock(UserDaoImpl.class);
        TeamDao teamDao = mock(TeamDaoImpl.class);

        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));

        TeamManager manager = new TeamManager(teamDao, userDao);
        manager.removeUserFromTeam(user.getUsername(), team.getId());

        assertFalse(team.hasMember(user.getUsername()));
        verify(teamDao).update(team);
    }

    @Test
    void removeUserFromTeam_UsernameAndTeamId_throwsNotFoundExceptionIfUserNotFound() {
        User user = new User("username", new HashSet<>(), Instant.now());
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());
        UserDao userDao = mock(UserDaoImpl.class);
        TeamDao teamDao = mock(TeamDaoImpl.class);

        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(teamDao.findById(team.getId())).thenReturn(Optional.of(team));

        TeamManager manager = new TeamManager(teamDao, userDao);
        assertThrows(
            NotFoundException.class,
            () -> manager.removeUserFromTeam(user.getUsername(), team.getId())
        );
    }
    
    @Test
    void removeUserFromTeam_UsernameAndTeamId_throwsNotFoundExceptionIfTeamNotFound() {
        User user = new User("username", new HashSet<>(), Instant.now());
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());
        UserDao userDao = mock(UserDaoImpl.class);
        TeamDao teamDao = mock(TeamDaoImpl.class);

        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(teamDao.findById(team.getId())).thenReturn(Optional.empty());

        TeamManager manager = new TeamManager(teamDao, userDao);
        assertThrows(
            NotFoundException.class,
            () -> manager.removeUserFromTeam(user.getUsername(), team.getId())
        );
    }
}
