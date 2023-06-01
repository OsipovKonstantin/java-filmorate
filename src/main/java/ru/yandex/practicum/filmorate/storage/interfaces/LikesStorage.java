package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.List;
import java.util.Set;

public interface LikesStorage {

    List<Long> loadPopularFilmsId(int count);

    void insertLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Set<Long> loadLikes(Long filmId);
}
