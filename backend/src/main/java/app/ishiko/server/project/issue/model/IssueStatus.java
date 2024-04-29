package app.ishiko.server.project.issue.model;

import app.ishiko.server.project.Project;
import jakarta.persistence.*;

@Entity
@Table(name = "status")
public class IssueStatus {
    @Id
    @SequenceGenerator(name = "status_id_seq", sequenceName = "status_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "status_id_seq")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project", nullable = false)
    private Project project;

    public IssueStatus() {}

    public IssueStatus(String name, Project project) {
        this.name = name;
        this.project = project;
    }

    public IssueStatus(Integer id, String name, Project project) {
        this.id = id;
        this.name = name;
        this.project = project;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IssueStatus other = (IssueStatus) obj;
        return id.equals(other.getId());
    }
}
