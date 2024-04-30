package dev.oscarrojas.issuetracker.team;

public interface TeamDao {
    
    Team update(Team team);

    Team create(Team team);

    void delete(String id);

}
