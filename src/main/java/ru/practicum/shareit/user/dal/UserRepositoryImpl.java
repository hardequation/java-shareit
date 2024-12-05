package ru.practicum.shareit.user.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<Integer, User> userStorage = new HashMap<>();

    @Override
    public Boolean contains(Integer id) {
        return userStorage.containsKey(id);
    }

    @Override
    public Boolean containsByEmail(String email) {
        return userStorage.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public User find(Integer id) {
        return userStorage.get(id);
    }

    @Override
    public User add(User newUser) {
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User update(User newUser) {
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public void remove(Integer id) {
        userStorage.remove(id);
    }
}
