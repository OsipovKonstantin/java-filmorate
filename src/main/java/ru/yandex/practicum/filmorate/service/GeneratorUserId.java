package ru.yandex.practicum.filmorate.service;

public class GeneratorUserId {
    private static long id = 1;

    public static long getId() {
        return id++;
    }
}
