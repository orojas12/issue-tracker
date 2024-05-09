package dev.oscarrojas.issuetracker.user;

import java.time.Instant;

public record UserDto(String id, String username, String firstName, String lastName, Instant dateCreated) {
}
