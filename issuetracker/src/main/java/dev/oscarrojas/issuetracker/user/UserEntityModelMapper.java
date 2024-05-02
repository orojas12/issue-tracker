package dev.oscarrojas.issuetracker.user;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserEntityModelMapper {

    public static User getEntity(UserModel model) {
        User user = new User();
        user.setUsername(model.getUsername());
        user.setDateCreated(model.getDateCreated());
        user.setRoles(model.getRoles().stream()
                .map((role) -> role.getId())
                .collect(Collectors.toSet()));
        return user;
    }

    public static Collection<User> getEntities(Collection<UserModel> models) {
        return models.stream().map((model) -> getEntity(model)).toList();
    }

    public static UserModel getModel(User user) {
        UserModel model = new UserModel();
        model.setUsername(user.getUsername());
        model.setDateCreated(user.getDateCreated());
        model.setRoles(user.getRoles().stream()
                .map((role) -> new RoleModel(role))
                .collect(Collectors.toSet()));
        return model;
    }

    public static Collection<UserModel> getModels(Collection<User> users) {
        return users.stream().map((user) -> getModel(user)).toList();
    }
}
