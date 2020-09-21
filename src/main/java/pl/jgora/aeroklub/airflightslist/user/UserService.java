package pl.jgora.aeroklub.airflightslist.user;

import pl.jgora.aeroklub.airflightslist.model.User;

import java.util.Set;

public interface UserService {
    User findByLogin(String login);
    void saveUser(User user);

    Set<User> findAll();

    User findById(Long id);

    void activationUpdate(User toDeactivate);
}