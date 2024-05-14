package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.util.SQLiteDateTimeConverter;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team")
public class TeamModel {

    @Id
    private String id;
    private String name;
    @Convert(converter = SQLiteDateTimeConverter.class)
    private Instant dateCreated;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamMemberModel> members = new HashSet<>();

    public TeamModel() {
    }

    public TeamModel(
            String id,
            String name,
            Instant dateCreated,
            Set<TeamMemberModel> members) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<TeamMemberModel> getMembers() {
        return members;
    }

    public void setMembers(Collection<TeamMemberModel> members) {
        this.members.clear();
        if (members != null) {
            this.members.addAll(members);
        }
    }

    public void addMember(TeamMemberModel member) {
        members.add(member);
    }

}
