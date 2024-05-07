package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {
    
    @Test
    void hasMember_Username_returnsTrueIfIsMember() {
        TeamMember teamMember = new TeamMember("user1");
        Team team = new Team("id", "team1", Instant.now(), new HashSet<>(Set.of(teamMember)));

        assertTrue(team.hasMember(teamMember.username()));
    }

    @Test
    void hasMember_Username_returnsFalseIfNotMember() {
        TeamMember teamMember1 = new TeamMember("user1");
        Team team = new Team("id", "team1", Instant.now(), new HashSet<>());

        assertFalse(team.hasMember(teamMember1.username()));

        TeamMember teamMember2 = new TeamMember("user2");
        team.getMembers().add(teamMember2);

        assertFalse(team.hasMember(teamMember1.username()));
    }

    @Test
    void getMember_Username_returnsMember() {
        TeamMember teamMember1 = new TeamMember("user1");
        TeamMember teamMember2 = new TeamMember("user2");
        Team team = new Team();
        team.setMembers(new HashSet<>(Set.of(teamMember1, teamMember2)));

        Optional<TeamMember> result1 = team.getMember(teamMember1.username());
        assertEquals(result1.get().username(), teamMember1.username());

        Optional<TeamMember> result2 = team.getMember(teamMember2.username());
        assertEquals(result2.get().username(), teamMember2.username());
    }

    @Test
    void addMember_User_addsMemberIfNotInTeam() throws DuplicateElementException {
        TeamMember teamMember1 = new TeamMember("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        team.addMember(teamMember1);
        assertTrue(team.getMembers().contains(teamMember1));
        assertEquals(team.getMembers().size(), 1);
    }

    @Test
    void addMember_User_throwsDuplicateElementExceptionIfAlreadyInTeam() {
        TeamMember teamMember1 = new TeamMember("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>(Set.of(teamMember1)));

        assertThrows(DuplicateElementException.class, () -> team.addMember(teamMember1));
    }

    @Test
    void removeMember_Username_removesMemberIfExists() throws NotFoundException {
        TeamMember teamMember1 = new TeamMember("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>(Set.of(teamMember1)));

        team.removeMember(teamMember1.username());

        assertFalse(team.getMembers().contains(teamMember1));
        assertEquals(team.getMembers().size(), 0);
    }

    @Test
    void removeMember_Username_throwsNotFoundExceptionIfNotMember() {
        TeamMember teamMember1 = new TeamMember("user1");
        Team team = new Team("id", "name", Instant.now(), new HashSet<>());

        assertThrows(NotFoundException.class, () -> team.removeMember(teamMember1.username()));

        TeamMember teamMember2 = new TeamMember("user2");
        team.getMembers().add(teamMember2);

        assertThrows(NotFoundException.class, () -> team.removeMember(teamMember1.username()));
    }
}
