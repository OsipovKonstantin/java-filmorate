package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    private long id;
    private final Set<Long> friends = new HashSet<>();
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public void addFriend(Long friendId) {
        this.friends.add(friendId);
    }

    public void deleteFriend(Long friendId) {
        this.friends.remove(friendId);
    }
}
