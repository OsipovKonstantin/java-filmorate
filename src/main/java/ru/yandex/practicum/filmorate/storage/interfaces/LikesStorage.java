package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.List;
import java.util.Set;

public interface LikesStorage {

    List<Long> getPopularFilmsId(int count);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Set<Long> getLikes(Long filmId);
}
