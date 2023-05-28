package ru.yandex.practicum.filmorate.exceptions;

public class FilmLikeNotFoundException extends RuntimeException {
    public FilmLikeNotFoundException(String message) {
        super(message);
    }
}
