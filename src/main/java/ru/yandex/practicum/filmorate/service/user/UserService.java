package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public User addUser(User user) {
        return userStorage.insertUser(user).toBuilder().friends(friendsStorage.loadFriends(user.getId())).build();
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user).toBuilder().friends(friendsStorage.loadFriends(user.getId())).build();
    }

    public List<User> getUsers() {
        return userStorage.loadUsers().stream()
                .peek(u -> u.setFriends(friendsStorage.loadFriends(u.getId())))
                .collect(Collectors.toList());
    }

    public User getUser(Long id) {
        return userStorage.loadUser(id).toBuilder().friends(friendsStorage.loadFriends(id)).build();
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }
}
