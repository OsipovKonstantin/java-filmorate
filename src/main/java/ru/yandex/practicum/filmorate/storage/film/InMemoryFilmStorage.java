package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GeneratorFilmId;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
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
        if (films
                .stream()
                .noneMatch(f -> f.getId() == film.getId()))
            throw new FilmNotFoundException(String.format("Невозможно обновить фильм. id %d не существует.",
                    film.getId()));

        for (int i = 0; i < films.size(); i++)
            if (films.get(i).getId() == film.getId())
                films.set(i, film);
        log.debug("Обновлен фильм {}.", film);
        return film;
    }

    @Override
    public void deleteFilmById(Long filmId) {
        Film film = getFilmById(filmId);
        if (film == null)
            throw new FilmNotFoundException(String.format("Фильма с id %d не существует.", filmId));
        films.remove(film);
    }

    @Override
    public List<Film> getFilms() {
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        if(films.stream().noneMatch(f -> f.getId() == id))
            throw new FilmNotFoundException(String.format("Фильма с id %s не существует.", id));

        return films.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
