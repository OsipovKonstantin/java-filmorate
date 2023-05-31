package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface LikesStorage {

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Set<Long> getLikesByFilmId(Long id);
}
