package dev.oscarrojas.issuetracker;

import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserModel;

import java.time.Instant;

public class TestUtils {

    public static User userWithUsername(String username) {
        return new User(username, Instant.now());
    }

    public static UserModel userModelWithUsername(String username) {
        return new UserModel(username, "password", Instant.now());
    }

}
