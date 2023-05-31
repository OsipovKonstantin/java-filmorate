package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Status;

import java.util.HashMap;

public interface FriendsStorage {
    void addFriend(Long id, Long friendId, Status status);

    HashMap<Long, Status> getFriendsByUserId(Long id);

    void updateStatus(Long id, Long friendId, Status status);

    void deleteFriend(Long id, Long friendId);
}
