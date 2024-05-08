package dev.oscarrojas.issuetracker.user;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserBuilderTest {

    @Test
    void id_setsId() {
        var builder = new UserBuilder();
        User user = builder.id("abc").build();
        assertEquals("abc", user.getId());
    }

    @Test
    void username_setsUsername() {
        var builder = new UserBuilder();
        User user = builder.username("user1").build();
        assertEquals("user1", user.getUsername());
    }

    @Test
    void firstName_setsFirstName() {
        var builder = new UserBuilder();
        User user = builder.firstName("john").build();
        assertEquals("john", user.getFirstName());
    }

    @Test
    void lastName_setsLastName() {
        var builder = new UserBuilder();
        User user = builder.lastName("wick").build();
        assertEquals("wick", user.getLastName());
    }

    @Test
    void dateCreated_setsDateCreated() {
        var builder = new UserBuilder();
        Instant instant = Instant.now();
        User user = builder.dateCreated(instant).build();
        assertEquals(instant, user.getDateCreated());
    }

    @Test
    void build_setsDateCreatedIfNotSet() {
        var builder = new UserBuilder();
        User user = builder.build();

        // dateCreated should be equal to today's date
        assertEquals(
                LocalDate.now(),
                user.getDateCreated().atZone(ZoneId.systemDefault()).toLocalDate());
    }

}
