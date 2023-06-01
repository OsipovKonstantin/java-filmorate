package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Long insertFilm(Film film);

    Film updateFilm(Film film);

    List<Film> loadFilms();

    Film getFilm(Long id);
}
