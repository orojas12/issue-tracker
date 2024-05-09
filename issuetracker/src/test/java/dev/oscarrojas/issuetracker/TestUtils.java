package dev.oscarrojas.issuetracker;

import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserBuilder;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.util.RandomStringGenerator;

import java.time.Instant;

public class TestUtils {

    public static User userWithUsername(String username) {
        return new UserBuilder()
                .id(RandomStringGenerator.getRandomString(10))
                .username(username)
                .firstName("John")
                .lastName("Wick")
                .dateCreated(Instant.now())
                .build();
    }

    public static UserModel userModelWithUsername(String username) {
        return new UserModel(
                RandomStringGenerator.getRandomString(10),
                username,
                "John",
                "Wick",
                Instant.now());
    }

}
