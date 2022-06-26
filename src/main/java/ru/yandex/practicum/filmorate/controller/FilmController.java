package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GeneratorFilmId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();

    @GetMapping
    public List<Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        commonCheckFilm(film);
        film.setId(GeneratorFilmId.getId());

        films.add(film);
        log.debug("Добавлен фильм {}.", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        commonCheckFilm(film);
        if (films.stream().noneMatch(f -> f.getId() == film.getId()))
            throw new ValidationException("Невозможно обновить фильм. id " + film.getId() + " не существует.");

        for (int i = 0; i < films.size(); i++)
            if (films.get(i).getId() == film.getId())
                films.set(i, film);
        log.debug("Обновлен фильм {}.", film);
        return film;
    }

    private void commonCheckFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank())
            throw new ValidationException("Название не может быть пустым. Введено: " + film.getName());
        if (film.getDescription() == null)
            throw new ValidationException("Описание не может быть пустым. Введено: " + film.getDescription());
        if (film.getDescription().length() > 200)
            throw new ValidationException("Максимальная длина описания — 200 символов. Введено: "
                    + film.getDescription().length() + " символов.");
        if (film.getReleaseDate() == null)
            throw new ValidationException("Дата релиза не может быть пустой. Введено: " + film.getReleaseDate());
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года. Введено: "
                    + film.getReleaseDate());
        if (film.getDuration() == null)
            throw new ValidationException("Поле продолжительность фильма не должно быть пустым. Введено: "
                    + film.getDuration());
        if (film.getDuration() <= 0)
            throw new ValidationException("Продолжительность фильма должна быть положительной. Введено: "
                    + film.getDuration());
    }
}