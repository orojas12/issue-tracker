package dev.oscarrojas.issuetracker.project.issue;

import dev.oscarrojas.issuetracker.data.SQLiteInstantConverter;
import dev.oscarrojas.issuetracker.data.SQLiteLocalDateTimeConverter;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
    @Convert(converter = SQLiteInstantConverter.class)
    private Instant createdAt;
    @Convert(converter = SQLiteLocalDateTimeConverter.class)
    private LocalDateTime dueDate;
    private ZoneId dueDateTimeZone;
    @Column(nullable = false)
    private boolean closed;

    public IssueModel() {
    }

    public IssueModel(Long id, String title, String description, Instant createdAt, LocalDateTime dueDate,
            ZoneId dueDateTimeZone, boolean closed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.dueDateTimeZone = dueDateTimeZone;
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

    @Nullable
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Nullable
    public ZoneId getDueDateTimeZone() {
        return dueDateTimeZone;
    }

    public void setDueDateTimeZone(ZoneId dueDateTimeZone) {
        this.dueDateTimeZone = dueDateTimeZone;
    }
}
