package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FriendAlreadyAddedException;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public void addFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        boolean isUserFriend = user.getFriends().containsKey(friendId);
        boolean ifFriendFriend = friend.getFriends().containsKey(id);

        if (isUserFriend && ifFriendFriend)
            throw new FriendAlreadyAddedException(String.format("%s добавлен в друзья к %s.",
                    friend.getName(), user.getName()));
        else if (isUserFriend)
            throw new FriendAlreadyAddedException(String.format("%s уже подал заявку в друзья к %s.",
                    friend.getName(), user.getName()));
        else if (ifFriendFriend){
            friendsStorage.addFriend(id, friendId, Status.CONFIRMED);
            friendsStorage.updateStatus(friendId, id, Status.CONFIRMED);
        } else
            friendsStorage.addFriend(id, friendId, Status.UNCONFIRMED);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);

        if (!user.getFriends().containsKey(friendId))
            throw new FriendNotFoundException(String.format("%s не является другом %s, поэтому не может быть удалён.",
                    user.getName(), friend.getName()));
        else if (user.getFriends().get(friendId).equals(Status.CONFIRMED))
            friendsStorage.updateStatus(friendId, id, Status.UNCONFIRMED);

        friendsStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getUserById(id).getFriends().entrySet().stream()
                .map(e->userStorage.getUserById(e.getKey()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = getFriends(id);
        commonFriends.retainAll(getFriends(otherId));
        return commonFriends;
    }
}