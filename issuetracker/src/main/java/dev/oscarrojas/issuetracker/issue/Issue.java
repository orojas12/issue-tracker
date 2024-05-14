package dev.oscarrojas.issuetracker.issue;

import java.time.Instant;

public class Issue {
    private Long id;
    private String title;
    private String description;
    private Instant createdAt;
    private Instant dueDate;
    private boolean closed;

    public Issue() {
    }

    public Issue(Long id, String title, String description, Instant createdAt, Instant dueDate, boolean closed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.closed = closed;
    }

    public void open() {
        this.closed = false;
    }

    public void close() {
        this.closed = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isOpen() {
        return !closed;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {

        private final Issue issue = new Issue();

        public Builder id(long id) {
            issue.setId(id);
            return this;
        }

        public Builder title(String title) {
            issue.setTitle(title);
            return this;
        }

        public Builder description(String description) {
            issue.setDescription(description);
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            issue.setCreatedAt(createdAt);
            return this;
        }

        public Builder dueDate(Instant dueDate) {
            issue.setDueDate(dueDate);
            return this;
        }

        public Builder closed() {
            issue.close();
            return this;
        }

        public Builder open() {
            issue.open();
            return this;
        }

        public Issue build() {
            if (issue.getCreatedAt() == null) {
                issue.setCreatedAt(Instant.now());
            }

            return issue;
        }
    }
}
