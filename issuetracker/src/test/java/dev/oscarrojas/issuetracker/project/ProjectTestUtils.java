package dev.oscarrojas.issuetracker.project;

import static dev.oscarrojas.issuetracker.user.UserTestUtils.userModel;
import static dev.oscarrojas.issuetracker.util.RandomStringGenerator.getRandomString;

import java.time.Instant;

import dev.oscarrojas.issuetracker.project.persistence.JpaProject;
import jakarta.annotation.Nullable;

public class ProjectTestUtils {

    public static JpaProject jpaProject(@Nullable String id) {
        return new JpaProject(
                id == null ? getRandomString(8) : id,
                "name",
                "desc",
                Instant.now(),
                userModel("user1"));
    }
}
