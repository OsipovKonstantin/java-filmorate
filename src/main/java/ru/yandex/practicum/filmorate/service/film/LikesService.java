package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmLikeAlreadyAddedException;
import ru.yandex.practicum.filmorate.exceptions.FilmLikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikesService {
    private final FilmService filmService;
    private final UserService userService;
    private final LikesStorage likesStorage;

    public LikesService(FilmService filmService,
                        UserService userService,
                        LikesStorage likesStorage) {
        this.filmService = filmService;
        this.userService = userService;
        this.likesStorage = likesStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmService.getFilm(filmId);
        User user = userService.getUser(userId);

        if (likesStorage.loadLikes(filmId).contains(userId))
            throw new FilmLikeAlreadyAddedException(String.format("Пользователь %s уже поставил лайк фильму %s.",
                    user.getName(), film.getName()));
        likesStorage.insertLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmService.getFilm(filmId);
        User user = userService.getUser(userId);

        if (!likesStorage.loadLikes(filmId).contains(userId)) {
            throw new FilmLikeNotFoundException(String.format("Пользователь %s не ставил лайк фильму %s, поэтому " +
                    "лайк не может быть удалён.", user.getName(), film.getName()));
        }
        likesStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return likesStorage.loadPopularFilmsId(count).stream()
                .map(i -> filmService.getFilm(i))
                .collect(Collectors.toList());
    }
}
