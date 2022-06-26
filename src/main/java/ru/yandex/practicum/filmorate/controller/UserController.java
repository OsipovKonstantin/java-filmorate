package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorUserId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        commonCheckUser(user);
        user.setId(GeneratorUserId.getId());

        users.add(user);
        log.debug("Добавлен пользователь {}.", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        commonCheckUser(user);
        if (users.stream().noneMatch(u -> u.getId() == user.getId()))
            throw new ValidationException("Невозможно обновить пользователя. id: " + user.getId() + " не существует.");

        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getId() == user.getId())
                users.set(i, user);
        log.debug("Обновлен пользователь {}.", user);
        return user;
    }

    private void commonCheckUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@"))
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @. " +
                    "Введено: " + user.getEmail());
        if (user.getLogin() == null || user.getLogin().isBlank())
            throw new ValidationException("Логин не может быть пустым и содержать пробелы. Введено: "
                    + user.getLogin());
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        if (user.getBirthday() == null)
            throw new ValidationException("День рождения не может быть пустым. Введено: " + user.getBirthday());
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть в будущем. Введено: " + user.getBirthday());
    }
}
