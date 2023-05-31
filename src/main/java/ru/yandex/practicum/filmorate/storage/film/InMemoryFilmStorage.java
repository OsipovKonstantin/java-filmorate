package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GeneratorFilmId;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> films = new ArrayList<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(GeneratorFilmId.getId());
        films.add(film);
        log.debug("Добавлен фильм {}.", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Integer position = Stream.iterate(0, i -> i < films.size(), i -> i + 1)
                .filter(i -> films.get(i).getId() == film.getId())
                .findFirst().orElse(null);

        if (position == null)
            throw new FilmNotFoundException(String.format("Невозможно обновить фильм. id %d не существует.",
                    film.getId()));

        films.set(position, film);
        log.debug("Обновлен фильм {}.", film);
        return film;
    }

    @Override
    public void deleteFilmById(Long filmId) {
        Film film = getFilmById(filmId);
        films.remove(film);
    }

    @Override
    public List<Film> getFilms() {
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = films.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);

        if (film == null)
            throw new FilmNotFoundException(String.format("Фильма с id %s не существует.", id));

        return film;
    }
}
