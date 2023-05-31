package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenresController {
    private final GenreStorage genresStorage;

    public GenresController(GenreStorage genresStorage) {
        this.genresStorage = genresStorage;
    }

    @GetMapping
    public List<Genre> getGenres() {
        return genresStorage.getGenres();
    }

    @GetMapping("{id}")
    public Genre getGenres(@PathVariable int id) {
        return genresStorage.getGenres(id);
    }
}
