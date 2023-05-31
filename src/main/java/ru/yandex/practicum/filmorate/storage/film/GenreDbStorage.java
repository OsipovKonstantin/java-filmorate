package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        Genre.valueOf(rs.getString("name")))
                .stream().sorted((g1, g2) -> g1.compareTo(g2)).collect(Collectors.toList());
    }

    @Override
    public Genre getGenres(int id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(sql, id);
        if (!genreRow.next())
            throw new GenreNotFoundException(String.format("Жанра с id %d не существует", id));
        return Genre.valueOf(genreRow.getString("name"));
    }

    @Override
    public List<Genre> getGenresByFilmId(Long id) {
        String sql = "SELECT * FROM genres AS g LEFT JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "LEFT JOIN films AS f ON fg.film_id = f.film_id WHERE f.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        Genre.valueOf(rs.getString("genres.name")), id)
                .stream().sorted((g1, g2) -> g1.compareTo(g2)).collect(Collectors.toList());
    }
}
