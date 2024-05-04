package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.user.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {
    
    @Test
    void hasMember_Username_returnsTrueIfIsMember() {
        User user = new User();
        user.setUsername("username");
        Team team = new Team("id", "name", Instant.now(), Set.of(user));

        assertTrue(team.hasMember(user.getUsername()));
    }

    @Test
    void hasMember_Username_returnsFalseIfNotMember() {
        User user1 = new User();
        user1.setUsername("username1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        assertFalse(team.hasMember(user1.getUsername()));

        User user2 = new User();
        user2.setUsername("username2");
        team.setMembers(Set.of(user2));

        assertFalse(team.hasMember(user1.getUsername()));
    }

    @Test
    void getMember_Username_returnsMember() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        Team team = new Team();
        team.setMembers(new HashSet<>(List.of(user1, user2)));

        Optional<User> result1 = team.getMember(user1.getUsername());
        assertEquals(result1.get().getUsername(), user1.getUsername());

        Optional<User> result2 = team.getMember(user2.getUsername());
        assertEquals(result2.get().getUsername(), user2.getUsername());
    }

    @Test
    void addMember_User_addsMemberIfNotMember() throws DuplicateElementException {
        User user = new User();
        user.setUsername("username");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        team.addMember(user);
        assertTrue(team.getMembers().contains(user));
        assertEquals(team.getMembers().size(), 1);
    }

    @Test
    void addMember_User_throwsDuplicateElementExceptionIfAlreadyMember() {
        User user = new User();
        Team team = new Team("id", "name", Instant.now(), Set.of(user));

        assertThrows(DuplicateElementException.class, () -> team.addMember(user));
    }

    @Test
    void removeMember_Username_removesMemberIfExists() throws NotFoundException {
        User user = new User();
        user.setUsername("username");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>(Set.of(user)));
        
        team.removeMember(user.getUsername());

        assertFalse(team.getMembers().contains(user));
        assertEquals(team.getMembers().size(), 0);
    }

    @Test
    void removeMember_Username_throwsNotFoundExceptionIfNotMember() {
        User user1 = new User();
        user1.setUsername("username1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        assertThrows(NotFoundException.class, () -> team.removeMember(user1.getUsername()));

        User user2 = new User();
        user2.setUsername("username2");
        team.setMembers(new HashSet<>(Set.of(user2)));

        assertThrows(NotFoundException.class, () -> team.removeMember(user1.getUsername()));
    }
}
