package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    Boolean contains(Integer id);

    Boolean containsByEmail(String email);

    User find(Integer id);

    User add(User user);

    User update(User newUser);

    void remove(Integer id);
}
