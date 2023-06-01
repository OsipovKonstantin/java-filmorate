package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpas() {
        String sql = "SELECT * FROM ratings";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        Mpa.valueOf(rs.getString("name")))
                .stream().sorted((g1, g2) -> g1.compareTo(g2)).collect(Collectors.toList());
    }

    @Override
    public Mpa getMpa(int id) {
        String sql = "SELECT * FROM ratings WHERE rating_id = ?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sql, id);
        if (!mpaRow.next())
            throw new MpaNotFoundException(String.format("Рейтинга MPA с id %d не существует", id));
        return Mpa.valueOf(mpaRow.getString("name"));
    }
}
