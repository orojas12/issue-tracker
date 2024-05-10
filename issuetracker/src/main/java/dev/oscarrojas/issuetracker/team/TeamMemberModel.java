package dev.oscarrojas.issuetracker.team;

import dev.oscarrojas.issuetracker.user.UserModel;
import jakarta.persistence.*;

@Entity
@Table(name = "team_member")
public class TeamMemberModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private UserModel user;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamModel team;

    public TeamMemberModel(Integer id, UserModel user, TeamModel team) {
        this.id = id;
        this.user = user;
        this.team = team;
    }

    public TeamMemberModel(UserModel user, TeamModel team) {
        this.user = user;
        this.team = team;
    }

    public TeamMemberModel(UserModel user) {
        this.user = user;
    }

    public TeamMemberModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public TeamModel getTeam() {
        return team;
    }

    public void setTeam(TeamModel team) {
        this.team = team;
    }
}
