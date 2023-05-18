package ru.yandex.practicum.filmorate.exceptions;

import javax.validation.constraints.NotBlank;

public class FilmLikeAlreadyAddedException extends RuntimeException {
    public FilmLikeAlreadyAddedException(String message) {
        super(message);
    }
}
