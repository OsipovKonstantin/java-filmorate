package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> loadUsers();

    User insertUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    User loadUser(Long id);
}
