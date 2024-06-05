package dev.oscarrojas.issuetracker.issue.persistence;

import dev.oscarrojas.issuetracker.data.SQLiteInstantConverter;
import dev.oscarrojas.issuetracker.issue.dto.IssueDetails;
import dev.oscarrojas.issuetracker.project.persistence.JpaProject;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "issue")
public class JpaIssue implements IssueDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(nullable = false)
    @Convert(converter = SQLiteInstantConverter.class)
    private Instant createdAt;
    @Column(nullable = false)
    private boolean open;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private JpaProject project;

    public JpaIssue() {
    }

    public JpaIssue(Long id, String title, String description, Instant createdAt,
            boolean open, JpaProject project) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.open = open;
        this.project = project;
    }

    @Override
    public Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public String getProjectId() {
        return project.getId();
    }

    public JpaProject getProject() {
        return project;
    }

    void setProject(JpaProject project) {
        this.project = project;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

}
