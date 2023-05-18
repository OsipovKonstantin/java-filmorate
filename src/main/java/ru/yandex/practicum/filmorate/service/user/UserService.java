package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FriendAlreadyAddedException;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Set<User> addFriend(Long id, Long friendId) {
        User[] twoUsers = checkIdsAndGetUsers(id, friendId);
        User user = twoUsers[0];
        User friend = twoUsers[1];

        if (user.getFriends().contains(friendId))
            throw new FriendAlreadyAddedException(String.format("%s уже добавлен в друзья %s.",
                    friend.getName(), user.getName()));

        user.addFriend(friendId);
        friend.addFriend(id);
        return user.getFriends().stream().map(userStorage::getUserById).collect(Collectors.toSet());
    }

    public Set<User> deleteFriend(Long id, Long friendId) {
        User[] twoUsers = checkIdsAndGetUsers(id, friendId);
        User user = twoUsers[0];
        User friend = twoUsers[1];

        if (!user.getFriends().contains(friendId))
            throw new FriendNotFoundException(String.format("%s не является другом %s, поэтому не может быть удалён.",
                    user.getName(), friend.getName()));

        user.deleteFriend(friendId);
        friend.deleteFriend(id);

        return user.getFriends().stream().map(userStorage::getUserById).collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        User[] twoUsers = checkIdsAndGetUsers(id, otherId);
        User user = twoUsers[0];
        User otherUser = twoUsers[1];

        Set<Long> commonFriends = new HashSet<>(user.getFriends());
        commonFriends.retainAll(otherUser.getFriends());

        return commonFriends.stream().map(userStorage::getUserById).collect(Collectors.toSet());
    }

    public User[] checkIdsAndGetUsers(Long id, Long otherId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(otherId);
        if (user == null)
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));
        else if (friend == null)
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", otherId));
        return new User[]{user, friend};
    }
}