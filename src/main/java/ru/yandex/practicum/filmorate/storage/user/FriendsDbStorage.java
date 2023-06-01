package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendsStorage;

import java.util.HashMap;

@Component
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertFriend(Long id, Long friendId, Status status) {
        String sql = "INSERT INTO friends (user_id, friend_id, status_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, status.getStatusId());
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public HashMap<Long, Status> loadFriends(Long id) {
        String sql = "SELECT f.friend_id, s.name " +
                "FROM friends f " +
                "LEFT JOIN status s ON f.status_id = s.status_id" +
                " WHERE f.user_id = ?";
        HashMap<Long, Status> userFriends = new HashMap<>();

        jdbcTemplate.query(sql, (rs, rowNum) -> {
            userFriends.put(rs.getLong("friend_id"), Status.valueOf(rs.getString("name")));
            return null;
        }, id);
        return userFriends;
    }

    @Override
    public void updateStatus(Long id, Long friendId, Status status) {
        String sql = "UPDATE friends SET status_id = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, status.getStatusId(), id, friendId);
    }
}
