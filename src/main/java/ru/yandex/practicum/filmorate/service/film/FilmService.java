package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmLikeAlreadyAddedException;
import ru.yandex.practicum.filmorate.exceptions.FilmLikeNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmAndUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Set<Long> addLike(Long filmId, Long userId) {
        FilmAndUser filmAndUser = checkIdsAndGetFilmAndUser(filmId, userId);
        Film film = filmAndUser.getFilm();
        User user = filmAndUser.getUser();

        if (film.getLikes().contains(userId))
            throw new FilmLikeAlreadyAddedException(String.format("Пользователь %s уже поставил лайк фильму %s.",
                    user.getName(), film.getName()));
        film.addLike(userId);
        return film.getLikes();
    }

    public Set<Long> deleteLike(Long filmId, Long userId) {
        FilmAndUser filmAndUser = checkIdsAndGetFilmAndUser(filmId, userId);
        Film film = filmAndUser.getFilm();
        User user = filmAndUser.getUser();

        if (!film.getLikes().contains(userId)) {
            throw new FilmLikeNotFoundException(String.format("Пользователь %s не ставил лайк фильму %s, поэтому " +
                    "лайк не может быть удалён.", user.getName(), film.getName()));
        }
        film.deleteLike(userId);
        return film.getLikes();
    }

    public List<Film> getNthPopularFilms(int count) {
        return filmStorage.getFilms().stream().sorted((f1, f2) -> f2.getLikes().size() -
                f1.getLikes().size()).limit(count).collect(Collectors.toList());
    }

    public FilmAndUser checkIdsAndGetFilmAndUser(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null)
            throw new FilmNotFoundException(String.format("Фильма с id %d не существует.", filmId));
        else if (user == null)
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", userId));
        return FilmAndUser.builder().film(film).user(user).build();
    }
}
