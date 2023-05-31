package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorUserId;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final List<User> users = new ArrayList<>();

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User addUser(User user) {
        user.setId(GeneratorUserId.getId());

        users.add(user);
        log.debug("Добавлен пользователь {}.", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Integer position = Stream.iterate(0, i -> i < users.size(), i -> i + 1)
                .filter(i -> users.get(i).getId() == user.getId())
                .findFirst()
                .orElse(null);

        if (position == null)
            throw new UserNotFoundException(String.format("Невозможно обновить пользователя. id: %d не существует.",
                    user.getId()));

        users.set(position, user);
        log.debug("Обновлен пользователь {}.", user);
        return user;
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = getUserById(userId);
        users.remove(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);

        if (user == null)
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));

        return user;
    }
}