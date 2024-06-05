package dev.oscarrojas.issuetracker.user;

import java.time.Instant;

import dev.oscarrojas.issuetracker.util.RandomStringGenerator;

public class UserTestUtils {

    public static User user(String username) {
        return new UserBuilder()
                .id(RandomStringGenerator.getRandomString(10))
                .username(username)
                .firstName("John")
                .lastName("Wick")
                .dateCreated(Instant.now())
                .build();
    }

    public static UserModel userModel(String username) {
        return new UserModel(
                RandomStringGenerator.getRandomString(10),
                username,
                "John",
                "Wick",
                Instant.now());
    }

}
