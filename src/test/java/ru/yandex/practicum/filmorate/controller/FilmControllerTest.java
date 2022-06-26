package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

class FilmControllerTest {
    private Validator validator;
    private final FilmController controller = new FilmController();

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void createFilm() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        controller.addFilm(film);

        Assertions.assertEquals(1, controller.getFilms().size(), "Количество фильмов не совпадает.");
        Assertions.assertEquals(film, controller.getFilms().get(0), "Фильмы не совпадают.");
    }

    @Test
    void createFilmWithBlankName() {
        Film film = Film.builder().name(" ").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void createFilmWithDescriptionLengthMoreThan200() {
        Film film = Film.builder().name("Титаник").description("Американский фильм-катастрофа 1997 года, снятый " +
                        "режиссёром Джеймсом Кэмероном, в котором показана гибель легендарного лайнера «Титаник». " +
                        "Герои фильма, будучи представителями различных социальных слоёв, влюбились друг в друга на " +
                        "борту лайнера, совершавшего свой первый и последний рейс через Атлантический океан в 1912 " +
                        "году. Главные роли исполнили Леонардо Ди Каприо и Кейт Уинслет.")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void createFilmWithReleaseDateBefore28DecemberOf1985() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1700, 1, 1)).duration(300L).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film));
        Assertions.assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года. Введено: "
                        + film.getReleaseDate(), e.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    void createFilmWithNotPositiveDuration() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(-100L).build();
        Film film2 = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(0L).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2);
        Assertions.assertFalse(violations2.isEmpty());
    }

    @Test
    void createFilmWithEmptyFilmOrWithoutNameOrDescriptionOrReleaseDateOrDuration() {
        Film filmEmpty = Film.builder().build();
        Film filmWithoutName = Film.builder().description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        Film filmWithoutDescription = Film.builder().name("Титаник")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        Film filmWithoutReleaseDate = Film.builder().name("Титаник").description("О крушении").duration(300L).build();
        Film filmWithoutDuration = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmEmpty);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<Film>> violations2 = validator.validate(filmWithoutName);
        Assertions.assertFalse(violations2.isEmpty());
        Set<ConstraintViolation<Film>> violations3 = validator.validate(filmWithoutDescription);
        Assertions.assertFalse(violations3.isEmpty());
        Set<ConstraintViolation<Film>> violations4 = validator.validate(filmWithoutReleaseDate);
        Assertions.assertFalse(violations4.isEmpty());
        Set<ConstraintViolation<Film>> violations5 = validator.validate(filmWithoutDuration);
        Assertions.assertFalse(violations5.isEmpty());
    }

    @Test
    void updateFilm() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        controller.addFilm(film);

        Film updatedFilm = film.toBuilder().name("Лайнер").description("О восстановлении")
                .releaseDate(LocalDate.of(2000, 2, 2)).duration(400L).build();
        controller.updateFilm(updatedFilm);

        Assertions.assertEquals(1, controller.getFilms().size(), "Количество фильмов не совпадает.");
        Assertions.assertEquals(updatedFilm, controller.getFilms().get(0), "Фильмы не совпадают.");
    }

    @Test
    void updateFilmWithBlankName() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().name(" ").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void updateFilmWithDescriptionLengthMoreThan200() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().description("Американский фильм-катастрофа 1997 года, снятый " +
                "режиссёром Джеймсом Кэмероном, в котором показана гибель легендарного лайнера «Титаник». " +
                "Герои фильма, будучи представителями различных социальных слоёв, влюбились друг в друга " +
                "на борту лайнера, совершавшего свой первый и последний рейс через Атлантический океан в " +
                "1912 году. Главные роли исполнили Леонардо Ди Каприо и Кейт Уинслет.").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void updateFilmWithReleaseDateBefore28DecemberOf1985() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().releaseDate(LocalDate.of(1700, 1, 1)).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года. Введено: "
                        + updatedFilm.getReleaseDate(), e.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateFilmWithNotPositiveDuration() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().duration(-100L).build();
        Film updatedFilm2 = film.toBuilder().duration(0L).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<Film>> violations2 = validator.validate(updatedFilm2);
        Assertions.assertFalse(violations2.isEmpty());
    }

    @Test
    void updateFilmWithNonexistentId() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().id(-1).build();
        Film updatedFilm2 = film.toBuilder().id(9999).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Невозможно обновить фильм. id " + updatedFilm.getId() + " не существует.",
                e.getMessage(), "Сообщения об ошибке не совпадают.");

        ValidationException e2 = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm2));
        Assertions.assertEquals("Невозможно обновить фильм. id " + updatedFilm2.getId() + " не существует.",
                e2.getMessage(), "Сообщения об ошибке не совпадают.");
    }


    @Test
    void updateFilmWithEmptyFilmOrWithoutNameOrDescriptionOrReleaseDateOrDuration() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        controller.addFilm(film);

        Film updatedFilmEmpty = Film.builder().id(film.getId()).build();
        Film updatedFilmWithoutName = Film.builder().id(film.getId()).description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        Film updatedFilmWithoutDescription = Film.builder().id(film.getId()).name("Титаник")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        Film updatedFilmWithoutReleaseDate = Film.builder().id(film.getId()).name("Титаник").description("О крушении")
                .duration(300L).build();
        Film updatedFilmWithoutDuration = Film.builder().id(film.getId()).name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilmEmpty);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<Film>> violations2 = validator.validate(updatedFilmWithoutName);
        Assertions.assertFalse(violations2.isEmpty());
        Set<ConstraintViolation<Film>> violations3 = validator.validate(updatedFilmWithoutDescription);
        Assertions.assertFalse(violations3.isEmpty());
        Set<ConstraintViolation<Film>> violations4 = validator.validate(updatedFilmWithoutReleaseDate);
        Assertions.assertFalse(violations4.isEmpty());
        Set<ConstraintViolation<Film>> violations5 = validator.validate(updatedFilmWithoutDuration);
        Assertions.assertFalse(violations5.isEmpty());
    }
}