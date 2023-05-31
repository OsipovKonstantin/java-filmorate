package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.HashMap;
import java.util.List;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         GenreStorage genreStorage,
                         FilmGenreStorage filmGenreStorage,
                         LikesStorage likesStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.likesStorage = likesStorage;
    }

    @Override
    public Film addFilm(Film film) {
        System.out.println(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        Long id = simpleJdbcInsert.executeAndReturnKey(new HashMap<String, Object>() {{
            put("film_id", film.getId());
            put("name", film.getName());
            put("description", film.getDescription());
            put("release_date", film.getReleaseDate());
            put("duration", film.getDuration());
            put("rating_id", film.getMpa().getId());
        }}).longValue();
        filmGenreStorage.addGenresToFilm(id, film.getGenres());
        return getFilmById(id);
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        filmGenreStorage.updateGenresToFilm(film.getId(), film.getGenres());
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films AS f LEFT JOIN ratings AS r ON f.rating_id = r.rating_id";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                Film.builder()
                        .id(rs.getLong("films.film_id"))
                        .likes(likesStorage.getLikesByFilmId(rs.getLong("films.film_id")))
                        .name(rs.getString("films.name"))
                        .description(rs.getString("films.description"))
                        .releaseDate(rs.getDate("films.release_date").toLocalDate())
                        .duration(rs.getLong("films.duration"))
                        .mpa(Mpa.valueOf(rs.getString("ratings.name")))
                        .genres(genreStorage.getGenresByFilmId(rs.getLong("films.film_id")))
                        .build());
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT * FROM films LEFT JOIN ratings ON films.rating_id = ratings.rating_id WHERE film_id = ?";
        try {
            return jdbcTemplate.query(sql, (rs, numRow) ->
                    Film.builder()
                            .id(rs.getLong("film_id"))
                            .likes(likesStorage.getLikesByFilmId(id))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .releaseDate(rs.getDate("release_date").toLocalDate())
                            .duration(rs.getLong("duration"))
                            .mpa(Mpa.valueOf(rs.getString("ratings.name")))
                            .genres(genreStorage.getGenresByFilmId(rs.getLong("film_id")))
                            .build(), id).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new FilmNotFoundException(String.format("Фильма с id %d не существует.", id));
        }
    }

    @Override
    public void deleteFilmById(Long filmId) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        int countOfDeletedRows = jdbcTemplate.update(sql, filmId);
        if (countOfDeletedRows == 0)
            throw new FilmNotFoundException(String.format("Фильма с id %s не существует, " +
                    "поэтому он не может быть удалён.", filmId));
    }
}