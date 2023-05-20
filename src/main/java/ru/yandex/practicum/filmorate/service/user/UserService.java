package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FriendAlreadyAddedException;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);

        if (user.getFriends().contains(friendId))
            throw new FriendAlreadyAddedException(String.format("%s уже добавлен в друзья %s.",
                    friend.getName(), user.getName()));

        user.addFriend(friendId);
        friend.addFriend(id);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);

        if (!user.getFriends().contains(friendId))
            throw new FriendNotFoundException(String.format("%s не является другом %s, поэтому не может быть удалён.",
                    user.getName(), friend.getName()));

        user.deleteFriend(friendId);
        friend.deleteFriend(id);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getUserById(id).getFriends().stream().map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);

        Set<Long> commonFriends = new HashSet<>(user.getFriends());
        commonFriends.retainAll(otherUser.getFriends());

        return commonFriends.stream().map(userStorage::getUserById).collect(Collectors.toList());
    }
}