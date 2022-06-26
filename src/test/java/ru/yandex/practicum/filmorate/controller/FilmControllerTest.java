package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerTest {
    private final FilmController controller = new FilmController();

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

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film));
        Assertions.assertEquals("Название не может быть пустым. Введено: " + film.getName(), e.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }

    @Test
    void createFilmWithDescriptionLengthMoreThan200() {
        Film film = Film.builder().name("Титаник").description("Американский фильм-катастрофа 1997 года, снятый " +
                        "режиссёром Джеймсом Кэмероном, в котором показана гибель легендарного лайнера «Титаник». " +
                        "Герои фильма, будучи представителями различных социальных слоёв, влюбились друг в друга на " +
                        "борту лайнера, совершавшего свой первый и последний рейс через Атлантический океан в 1912 " +
                        "году. Главные роли исполнили Леонардо Ди Каприо и Кейт Уинслет.")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film));
        Assertions.assertEquals("Максимальная длина описания — 200 символов. Введено: "
                        + film.getDescription().length() + " символов.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");
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

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной. Введено: "
                        + film.getDuration(), e.getMessage(), "Сообщения об ошибке не совпадают.");

        ValidationException e2 = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film2));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной. Введено: "
                        + film2.getDuration(), e2.getMessage(),
                "Сообщения об ошибке не совпадают.");
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

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(filmEmpty));
        Assertions.assertEquals("Название не может быть пустым. Введено: " + filmEmpty.getName(), e.getMessage(),
                "Сообщения об ошибке не совпадают.");
        ValidationException e2 = Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(filmWithoutName));
        Assertions.assertEquals("Название не может быть пустым. Введено: " + filmWithoutName.getName(),
                e2.getMessage(), "Сообщения об ошибке не совпадают.");
        ValidationException e3 = Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(filmWithoutDescription));
        Assertions.assertEquals("Описание не может быть пустым. Введено: "
                        + filmWithoutDescription.getDescription(), e3.getMessage(),
                "Сообщения об ошибке не совпадают.");
        ValidationException e4 = Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(filmWithoutReleaseDate));
        Assertions.assertEquals("Дата релиза не может быть пустой. Введено: "
                        + filmWithoutReleaseDate.getReleaseDate(), e4.getMessage(),
                "Сообщения об ошибке не совпадают.");
        ValidationException e5 = Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(filmWithoutDuration));
        Assertions.assertEquals("Поле продолжительность фильма не должно быть пустым. Введено: "
                        + filmWithoutDuration.getDuration(), e5.getMessage(),
                "Сообщения об ошибке не совпадают.");
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

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Название не может быть пустым. Введено: " + updatedFilm.getName(),
                e.getMessage(), "Сообщения об ошибке не совпадают.");
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

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Максимальная длина описания — 200 символов. Введено: "
                        + updatedFilm.getDescription().length() + " символов.", e.getMessage(),
                "Сообщения об ошибке не совпадают.");
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

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной. Введено: "
                        + updatedFilm.getDuration(), e.getMessage(), "Сообщения об ошибке не совпадают.");

        ValidationException e2 = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm2));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной. Введено: "
                        + updatedFilm2.getDuration(), e2.getMessage(), "Сообщения об ошибке не совпадают.");
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

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilmEmpty));
        Assertions.assertEquals("Название не может быть пустым. Введено: " + updatedFilmEmpty.getName(),
                e.getMessage(), "Сообщения об ошибке не совпадают.");
        ValidationException e2 = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilmWithoutName));
        Assertions.assertEquals("Название не может быть пустым. Введено: " + updatedFilmWithoutName.getName(),
                e2.getMessage(), "Сообщения об ошибке не совпадают.");
        ValidationException e3 = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilmWithoutDescription));
        Assertions.assertEquals("Описание не может быть пустым. Введено: "
                        + updatedFilmWithoutDescription.getDescription(), e3.getMessage(),
                "Сообщения об ошибке не совпадают.");
        ValidationException e4 = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilmWithoutReleaseDate));
        Assertions.assertEquals("Дата релиза не может быть пустой. Введено: "
                        + updatedFilmWithoutReleaseDate.getReleaseDate(), e4.getMessage(),
                "Сообщения об ошибке не совпадают.");
        ValidationException e5 = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilmWithoutDuration));
        Assertions.assertEquals("Поле продолжительность фильма не должно быть пустым. Введено: "
                        + updatedFilmWithoutDuration.getDuration(), e5.getMessage(),
                "Сообщения об ошибке не совпадают.");
    }
}