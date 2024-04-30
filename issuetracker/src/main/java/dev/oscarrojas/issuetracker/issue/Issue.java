package dev.oscarrojas.issuetracker.issue;

import java.time.Instant;
import java.util.Set;

import dev.oscarrojas.issuetracker.user.User;
import jakarta.annotation.Nullable;

public class Issue {

    private String id;
    private String title;
    private String description;
    private Instant creationDate;
    private @Nullable Instant dueDate;
    private boolean isClosed;
    private Set<User> assignees;

    public Issue() {
    }

    public Issue(String id, String title, String description, 
            Instant creationDate, @Nullable Instant dueDate, 
            boolean isClosed, Set<User> assignees) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.isClosed = isClosed;
        this.assignees = assignees;
    }

    public void close() {
        isClosed = true;
    }

    public void open() {
        isClosed = false;
    }

    public void assignUser(User user) {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public Set<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<User> assignments) {
        this.assignees = assignments;
    }

}
