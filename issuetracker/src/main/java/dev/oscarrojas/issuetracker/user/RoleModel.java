package dev.oscarrojas.issuetracker.user;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class RoleModel {
    
    @Id
    private String id;
    @ManyToMany(mappedBy = "roles")
    private Set<UserModel> users;

    public RoleModel() {}

    public RoleModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
