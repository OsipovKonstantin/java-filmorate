package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FriendAlreadyAddedException;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendsStorage;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendsService {
    private final UserService userService;
    private final FriendsStorage friendsStorage;

    public FriendsService(UserService userService, FriendsStorage friendsStorage) {
        this.userService = userService;
        this.friendsStorage = friendsStorage;
    }

    public void addFriend(Long id, Long friendId) {
        User user = userService.getUser(id);
        User friend = userService.getUser(friendId);
        boolean isUserFriend = friendsStorage.getFriends(id).containsKey(friendId);
        boolean ifFriendFriend = friendsStorage.getFriends(friendId).containsKey(id);

        if (isUserFriend && ifFriendFriend)
            throw new FriendAlreadyAddedException(String.format("%s добавлен в друзья к %s.",
                    friend.getName(), user.getName()));
        else if (isUserFriend)
            throw new FriendAlreadyAddedException(String.format("%s уже подал заявку в друзья к %s.",
                    friend.getName(), user.getName()));
        else if (ifFriendFriend) {
            friendsStorage.addFriend(id, friendId, Status.CONFIRMED);
            friendsStorage.updateStatus(friendId, id, Status.CONFIRMED);
        } else
            friendsStorage.addFriend(id, friendId, Status.UNCONFIRMED);
    }

    public void deleteFriend(Long id, Long friendId) {
        HashMap<Long, Status> userFriends = friendsStorage.getFriends(id);
        if (!userFriends.containsKey(friendId))
            throw new FriendNotFoundException(String.format("%s не является другом %s, поэтому не может быть удалён.",
                    userService.getUser(id).getName(), userService.getUser(friendId).getName()));
        else if (userFriends.get(friendId).equals(Status.CONFIRMED))
            friendsStorage.updateStatus(friendId, id, Status.UNCONFIRMED);

        friendsStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(Long id) {
        return friendsStorage.getFriends(id).keySet().stream()
                .map(userService::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = getFriends(id);
        commonFriends.retainAll(getFriends(otherId));
        return commonFriends;
    }
}