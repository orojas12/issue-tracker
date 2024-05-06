package dev.oscarrojas.issuetracker.user;

import java.util.Collection;

public class UserEntityModelMapper {

    public static User getEntity(UserModel model) {
        User user = new User();
        user.setUsername(model.getUsername());
        user.setDateCreated(model.getDateCreated());
        return user;
    }

    public static Collection<User> getEntities(Collection<UserModel> models) {
        return models.stream().map((model) -> getEntity(model)).toList();
    }

    public static UserModel getModel(User user) {
        UserModel model = new UserModel();
        model.setUsername(user.getUsername());
        model.setDateCreated(user.getDateCreated());
        return model;
    }

    public static Collection<UserModel> getModels(Collection<User> users) {
        return users.stream().map((user) -> getModel(user)).toList();
    }
}
