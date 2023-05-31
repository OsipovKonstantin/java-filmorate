package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmGenreStorage {

    void addGenresToFilm(Long id, List<Genre> genres);

    void updateGenresToFilm(long filmId, List<Genre> genres);
}
