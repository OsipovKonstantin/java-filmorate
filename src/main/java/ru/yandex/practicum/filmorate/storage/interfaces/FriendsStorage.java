package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Status;

import java.util.HashMap;

public interface FriendsStorage {
    void insertFriend(Long id, Long friendId, Status status);

    HashMap<Long, Status> loadFriends(Long id);

    void updateStatus(Long id, Long friendId, Status status);

    void deleteFriend(Long id, Long friendId);
}
