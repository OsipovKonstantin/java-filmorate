package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmLikeAlreadyAddedException;
import ru.yandex.practicum.filmorate.exceptions.FilmLikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        if (film.getLikes().contains(userId))
            throw new FilmLikeAlreadyAddedException(String.format("Пользователь %s уже поставил лайк фильму %s.",
                    user.getName(), film.getName()));
        likesStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        if (!film.getLikes().contains(userId)) {
            throw new FilmLikeNotFoundException(String.format("Пользователь %s не ставил лайк фильму %s, поэтому " +
                    "лайк не может быть удалён.", user.getName(), film.getName()));
        }
        likesStorage.deleteLike(filmId, userId);
    }

    public List<Film> getNthPopularFilms(int count) {
        return filmStorage.getFilms().stream().sorted((f1, f2) -> f2.getLikes().size() -
                f1.getLikes().size()).limit(count).collect(Collectors.toList());
    }
}
