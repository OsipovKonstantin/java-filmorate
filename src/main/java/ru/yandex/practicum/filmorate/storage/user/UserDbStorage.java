package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.List;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendsStorage friendsStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendsStorage = friendsStorage;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Long id = simpleJdbcInsert.executeAndReturnKey(new HashMap<String, Object>() {{
            put("email", user.getEmail());
            put("login", user.getLogin());
            put("name", user.getName());
            put("birthday", user.getBirthday());
        }}).longValue();
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return getUserById(user.getId());
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                User.builder().id(rs.getLong("user_id"))
                        .friends(friendsStorage.getFriendsByUserId(rs.getLong("user_id")))
                        .email(rs.getString("email"))
                        .login(rs.getString("login"))
                        .name(rs.getString("name"))
                        .birthday(rs.getDate("birthday").toLocalDate())
                        .build());
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            return User.builder().id(userRows.getLong("user_id"))
                    .friends(friendsStorage.getFriendsByUserId(id))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .build();
        } else {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));
        }
    }

    @Override
    public void deleteUserById(Long userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        int countOfDeletedRows = jdbcTemplate.update(sql, userId);
        if (countOfDeletedRows == 0)
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует, " +
                    "поэтому он не может быть удалён.", userId));
    }
}
