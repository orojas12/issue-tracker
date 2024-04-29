package app.ishiko.server.project.issue.model;

import app.ishiko.server.project.Project;
import jakarta.persistence.*;

@Entity
@Table(name = "label")
public class IssueLabel {
    @Id
    @SequenceGenerator(name = "label_id_seq", sequenceName = "label_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "label_id_seq")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project", nullable = false)
    private Project project;

    public IssueLabel() {}

    public IssueLabel(String name, Project project) {
        this.name = name;
        this.project = project;
    }

    public IssueLabel(Integer id, String name, Project project) {
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
        IssueLabel other = (IssueLabel) obj;
        return id.equals(other.getId());
    }

    
}
