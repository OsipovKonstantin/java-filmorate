package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmLikeAlreadyAddedException;
import ru.yandex.practicum.filmorate.exceptions.FilmLikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        if (film.getLikes().contains(userId))
            throw new FilmLikeAlreadyAddedException(String.format("Пользователь %s уже поставил лайк фильму %s.",
                    user.getName(), film.getName()));
        film.addLike(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        if (!film.getLikes().contains(userId)) {
            throw new FilmLikeNotFoundException(String.format("Пользователь %s не ставил лайк фильму %s, поэтому " +
                    "лайк не может быть удалён.", user.getName(), film.getName()));
        }
        film.deleteLike(userId);
    }

    public List<Film> getNthPopularFilms(int count) {
        return filmStorage.getFilms().stream().sorted((f1, f2) -> f2.getLikes().size() -
                f1.getLikes().size()).limit(count).collect(Collectors.toList());
    }
}
