package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerTest {
    private FilmController controller;

    public FilmControllerTest() {
        controller = new FilmController();
    }

    @Test
    void createFilm() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        controller.addFilm(film);
        Assertions.assertEquals(1, controller.getFilms().size(), "Количество фильмов не совпадает.");
        Assertions.assertEquals(film, controller.getFilms().get(0), "Фильмы не совпадают.");
    }

    @Test
    void createFilmWithBlankName() {
        Film film = Film.builder().name(" ").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film));
        Assertions.assertEquals("Название не может быть пустым.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void createFilmWithDescriptionLengthMoreThan200() {
        Film film = Film.builder().name("Титаник").description("Американский фильм-катастрофа 1997 года, снятый " +
                        "режиссёром Джеймсом Кэмероном, в котором показана гибель легендарного лайнера «Титаник». " +
                        "Герои фильма, будучи представителями различных социальных слоёв, влюбились друг в друга на " +
                        "борту лайнера, совершавшего свой первый и последний рейс через Атлантический океан в 1912 " +
                        "году. Главные роли исполнили Леонардо Ди Каприо и Кейт Уинслет.")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film));
        Assertions.assertEquals("Максимальная длина описания — 200 символов.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void createFilmWithReleaseDateBefore28DecemberOf1985() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1700, 1, 1)).duration(300).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film));
        Assertions.assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void createFilmWithNotPositiveDuration() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(-100).build();
        Film film2 = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(0).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");

        ValidationException e2 = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film2));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.", e2.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void createFilmWithEmptyFilmOrWithoutNameOrDescriptionOrReleaseDateOrDuration() {
        Film filmEmpty = Film.builder().build();
        Film filmWithoutName = Film.builder().description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        Film filmWithoutDescription = Film.builder().name("Титаник")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        Film filmWithoutReleaseDate = Film.builder().name("Титаник").description("О крушении").duration(300).build();
        Film filmWithoutDuration = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).build();

        NullPointerException e = Assertions.assertThrows(NullPointerException.class,
                () -> controller.addFilm(filmEmpty));
        Assertions.assertEquals(NullPointerException.class, e.getClass(), "Не совпадает название ошибки.");
        NullPointerException e2 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.addFilm(filmWithoutName));
        Assertions.assertEquals(NullPointerException.class, e2.getClass(), "Не совпадает название ошибки.");
        NullPointerException e3 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.addFilm(filmWithoutDescription));
        Assertions.assertEquals(NullPointerException.class, e3.getClass(), "Не совпадает название ошибки.");
        NullPointerException e4 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.addFilm(filmWithoutReleaseDate));
        Assertions.assertEquals(NullPointerException.class, e4.getClass(), "Не совпадает название ошибки.");
        ValidationException e5 = Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(filmWithoutDuration));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.", e5.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateFilm() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        controller.addFilm(film);

        Film updatedFilm = film.toBuilder().name("Лайнер").description("О восстановлении")
                .releaseDate(LocalDate.of(2000, 2, 2)).duration(400).build();
        controller.updateFilm(updatedFilm);

        Assertions.assertEquals(1, controller.getFilms().size(), "Количество фильмов не совпадает.");
        Assertions.assertEquals(updatedFilm, controller.getFilms().get(0), "Фильмы не совпадают.");
    }

    @Test
    void updateFilmWithBlankName() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().name(" ").build();

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Название не может быть пустым.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateFilmWithDescriptionLengthMoreThan200() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().description("Американский фильм-катастрофа 1997 года, снятый " +
                "режиссёром Джеймсом Кэмероном, в котором показана гибель легендарного лайнера «Титаник». " +
                "Герои фильма, будучи представителями различных социальных слоёв, влюбились друг в друга " +
                "на борту лайнера, совершавшего свой первый и последний рейс через Атлантический океан в " +
                "1912 году. Главные роли исполнили Леонардо Ди Каприо и Кейт Уинслет.").build();

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Максимальная длина описания — 200 символов.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateFilmWithReleaseDateBefore28DecemberOf1985() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().releaseDate(LocalDate.of(1700, 1, 1)).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateFilmWithNotPositiveDuration() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().duration(-100).build();
        Film updatedFilm2 = film.toBuilder().duration(0).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");

        ValidationException e2 = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm2));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.", e2.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateFilmWithNonexistentId() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
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
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        controller.addFilm(film);

        Film updatedFilmEmpty = Film.builder().id(film.getId()).build();
        Film updatedFilmWithoutName = Film.builder().id(film.getId()).description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        Film updatedFilmWithoutDescription = Film.builder().id(film.getId()).name("Титаник")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300).build();
        Film updatedFilmWithoutReleaseDate = Film.builder().id(film.getId()).name("Титаник").description("О крушении")
                .duration(300).build();
        Film updatedFilmWithoutDuration = Film.builder().id(film.getId()).name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).build();

        NullPointerException e = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateFilm(updatedFilmEmpty));
        Assertions.assertEquals(NullPointerException.class, e.getClass(), "Не совпадает название ошибки.");
        NullPointerException e2 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateFilm(updatedFilmWithoutName));
        Assertions.assertEquals(NullPointerException.class, e2.getClass(), "Не совпадает название ошибки.");
        NullPointerException e3 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateFilm(updatedFilmWithoutDescription));
        Assertions.assertEquals(NullPointerException.class, e3.getClass(), "Не совпадает название ошибки.");
        NullPointerException e4 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateFilm(updatedFilmWithoutReleaseDate));
        Assertions.assertEquals(NullPointerException.class, e4.getClass(), "Не совпадает название ошибки.");
        ValidationException e5 = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilmWithoutDuration));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.", e5.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }
}