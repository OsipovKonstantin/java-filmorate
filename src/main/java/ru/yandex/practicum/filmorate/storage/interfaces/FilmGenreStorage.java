package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

public interface FilmGenreStorage {
    void insertGenres(long filmId, LinkedHashSet<Genre> genres);

    void updateGenres(long filmId, LinkedHashSet<Genre> genres);

    LinkedHashSet<Genre> loadGenres(long filmId);
}
