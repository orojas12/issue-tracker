package dev.oscarrojas.issuetracker;

import dev.oscarrojas.issuetracker.issue.Issue;
import dev.oscarrojas.issuetracker.issue.IssueModel;
import dev.oscarrojas.issuetracker.user.User;
import dev.oscarrojas.issuetracker.user.UserBuilder;
import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.util.RandomStringGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

    public static Issue issue(Long id) {
        return Issue.getBuilder().id(id).title("title").description("desc")
                .createdAt(Instant.now()).build();
    }

    public static IssueModel issueModel(Long id) {
        return new IssueModel(null, "title", "desc", Instant.now(), LocalDateTime.now(), ZoneId.systemDefault(), false);
    }

}
