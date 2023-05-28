package ru.yandex.practicum.filmorate.exceptions;

public class FilmLikeAlreadyAddedException extends RuntimeException {
    public FilmLikeAlreadyAddedException(String message) {
        super(message);
    }
}
