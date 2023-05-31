package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    UNCONFIRMED((byte) 1, "неподтверждённая"),
    CONFIRMED((byte) 2, "подтверждённая");

    private final byte statusId;
    private final String name;
}
