package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.util.SQLiteDateTimeConverter;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "issue")
public class IssueModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(nullable = false)
    @Convert(converter = SQLiteDateTimeConverter.class)
    private Instant createdAt;
    @Convert(converter = SQLiteDateTimeConverter.class)
    private Instant dueDate;
    @Column(nullable = false)
    private boolean closed;

    public IssueModel() {
    }

    public IssueModel(Long id, String title, String description, Instant createdAt, Instant dueDate, boolean closed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.closed = closed;
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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
