package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;

import java.util.LinkedHashSet;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertGenres(long filmId, LinkedHashSet<Genre> genres) {
        String sql = "INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES (?, ?)";
        if (genres != null)
            for (Genre genre : genres)
                jdbcTemplate.update(sql, filmId, genre.getId());
    }

    public void updateGenres(long filmId, LinkedHashSet<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        insertGenres(filmId, genres);
    }

    @Override
    public LinkedHashSet<Genre> loadGenres(long filmId) {
        String sql = "SELECT g.name " +
                "FROM film_genre fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?";
        return new LinkedHashSet<>(jdbcTemplate.query(sql, (rs, ronNum) ->
                Genre.valueOf(rs.getString("name")), filmId));
    }
}
