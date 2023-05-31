package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenresToFilm(Long id, List<Genre> genres) {
        String sql = "INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES (?, ?)";
        if (genres != null)
            for (Genre genre : new HashSet<>(genres))
                jdbcTemplate.update(sql, id, genre.getId());
    }

    @Override
    public void updateGenresToFilm(long filmId, List<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        addGenresToFilm(filmId, genres);
    }
}
