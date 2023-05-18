package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorUserId;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
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
        if (users.stream().noneMatch(u -> u.getId() == user.getId()))
            throw new UserNotFoundException(String.format("Невозможно обновить пользователя. id: %d не существует.",
                    user.getId()));

        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getId() == user.getId())
                users.set(i, user);
        log.debug("Обновлен пользователь {}.", user);
        return user;
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = getUserById(userId);
        if (user == null)
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", userId));
        users.remove(user);
    }

    @Override
    public User getUserById(Long id) {
        if (users.stream().noneMatch(u -> u.getId() == id))
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));

        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
