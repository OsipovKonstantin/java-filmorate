package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       FilmGenreStorage filmGenreStorage,
                       LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.likesStorage = likesStorage;
    }

    public Film addFilm(Film film) {
        Long id = filmStorage.addFilm(film);
        filmGenreStorage.addGenres(id, film.getGenres());
        return getFilm(id);
    }

    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        filmGenreStorage.updateGenres(film.getId(), film.getGenres());
        return getFilm(film.getId());
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms().stream()
                .map(f -> f.toBuilder()
                        .likes(likesStorage.getLikes(f.getId()))
                        .genres(filmGenreStorage.getGenres(f.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id).toBuilder()
                .likes(likesStorage.getLikes(id))
                .genres(filmGenreStorage.getGenres(id))
                .build();
    }
}
