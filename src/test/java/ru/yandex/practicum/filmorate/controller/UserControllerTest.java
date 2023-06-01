package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    private Validator validator;
    private final UserController controller;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void createUser() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).friends(new HashMap<>()).build();
        User addedUser = controller.addUser(user);

        Assertions.assertEquals(1, controller.getUsers().size(), "Неверное количество пользователей.");
        Assertions.assertEquals(user.getEmail(), controller.getUsers().get(0).getEmail(),
                "Почты не совпадают.");
        Assertions.assertEquals(user.getLogin(), controller.getUsers().get(0).getLogin(),
                "Логины не совпадают.");
        Assertions.assertEquals(user.getName(), controller.getUsers().get(0).getName(),
                "Имена не совпадают.");
        Assertions.assertEquals(user.getBirthday(), controller.getUsers().get(0).getBirthday(),
                "Дни рождения не совпадают.");
        Assertions.assertEquals(user.getFriends(), controller.getUsers().get(0).getFriends(),
                "Список друзей не совпадает.");
    }

    @Test
    void createUserWithBlankEmailOrWithEmailWithoutAtSign() {
        User user = User.builder().email(" ").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User user2 = User.builder().email("bigmanya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<User>> violations2 = validator.validate(user2);
        Assertions.assertFalse(violations2.isEmpty());
    }

    @Test
    void createUserWithBlankLogin() {
        User user = User.builder().email("bigman@ya.ru").login(" ").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void createUserWithBlankName() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name(" ")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        Assertions.assertEquals(1, controller.getUsers().size(), "Неверное количество пользователей.");
        Assertions.assertEquals(user.getLogin(), controller.getUsers().get(0).getName(),
                "Имя пользователя должно быть равно логину в случае передачи пустого имени пользователя.");
    }

    @Test
    void createUserWithBirthdayInFuture() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(2100, 1, 1)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void createUserWithEmptyUserOrWithoutLoginOrEmailOrNameOrBirthday() {
        User userEmpty = User.builder().build();
        User userWithoutLogin = User.builder().email("bigman@ya.ru").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User userWithoutEmail = User.builder().login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User userWithoutName = User.builder().email("bigman@ya.ru").login("BigMan")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User userWithoutBirthday = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish").build();

        Set<ConstraintViolation<User>> violations = validator.validate(userEmpty);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<User>> violations2 = validator.validate(userWithoutLogin);
        Assertions.assertFalse(violations2.isEmpty());
        Set<ConstraintViolation<User>> violations3 = validator.validate(userWithoutEmail);
        Assertions.assertFalse(violations3.isEmpty());
        controller.addUser(userWithoutName);
        Assertions.assertEquals(userWithoutName.getLogin(), controller.getUsers().get(0).getName());
        Set<ConstraintViolation<User>> violations4 = validator.validate(userWithoutBirthday);
        Assertions.assertFalse(violations4.isEmpty());
    }

    @Test
    void updateUser() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).friends(new HashMap<>()).build();
        User addedUser = controller.addUser(user);

        User updatedUser = user.toBuilder().id(addedUser.getId()).email("smallwoman@google.com").login("SmallWoman")
                .name("Womanish").birthday(LocalDate.of(2004, 12, 12)).friends(new HashMap<>())
                .build();
        controller.updateUser(updatedUser);

        Assertions.assertEquals(1, controller.getUsers().size(), "Неверное количество пользователей.");
        Assertions.assertEquals(updatedUser.getEmail(), controller.getUsers().get(0).getEmail(),
                "Почты не совпадают.");
        Assertions.assertEquals(updatedUser.getLogin(), controller.getUsers().get(0).getLogin(),
                "Логины не совпадают.");
        Assertions.assertEquals(updatedUser.getName(), controller.getUsers().get(0).getName(),
                "Имена не совпадают.");
        Assertions.assertEquals(updatedUser.getBirthday(), controller.getUsers().get(0).getBirthday(),
                "Дни рождения не совпадают.");
        Assertions.assertEquals(updatedUser.getFriends(), controller.getUsers().get(0).getFriends(),
                "Список друзей не совпадает.");
    }

    @Test
    void updateUserWithBlankEmailOrWithEmailWithoutAtSign() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        User updatedUser = user.toBuilder().email(" ").login("SmallWoman").name("Womanish")
                .birthday(LocalDate.of(2004, 12, 12)).build();
        User updatedUser2 = user.toBuilder().email("smallwomangoogle.com").login("SmallWoman").name("Womanish")
                .birthday(LocalDate.of(2004, 12, 12)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(updatedUser);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<User>> violations2 = validator.validate(updatedUser2);
        Assertions.assertFalse(violations2.isEmpty());
    }

    @Test
    void updateUserWithBlankLogin() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        User updatedUser = user.toBuilder().email("smallwoman@google.com").login(" ").name("Womanish")
                .birthday(LocalDate.of(2004, 12, 12)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(updatedUser);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void updateUserWithBlankName() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User addedUser = controller.addUser(user);

        User updatedUser = user.toBuilder().id(addedUser.getId()).email("smallwoman@google.com").login("SmallWoman")
                .name(" ").birthday(LocalDate.of(2004, 12, 12)).build();
        controller.updateUser(updatedUser);

        Assertions.assertEquals(1, controller.getUsers().size(), "Неверное количество пользователей.");
        Assertions.assertEquals(updatedUser.getLogin(), controller.getUsers().get(0).getName(),
                "Имя пользователя должно быть равно логину в случае передачи пустого имени пользователя.");
    }

    @Test
    void updateUserWithBirthdayInFuture() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        User updatedUser = user.toBuilder().email("smallwoman@google.com").login("SmallWoman").name("Womanish")
                .birthday(LocalDate.of(2100, 12, 12)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(updatedUser);
        Assertions.assertFalse(violations.isEmpty());
    }


    @Test
    void updateUserWithNonexistentId() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        User updatedUser = user.toBuilder().id(9999).build();

        UserNotFoundException e = Assertions.assertThrows(UserNotFoundException.class,
                () -> controller.updateUser(updatedUser));
        Assertions.assertEquals("Пользователя с id " + updatedUser.getId()
                + " не существует.", e.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateUserWithEmptyUserOrWithoutLoginOrEmailOrNameOrBirthday() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User addedUser = controller.addUser(user);

        User updatedUserEmpty = User.builder().id(addedUser.getId()).build();
        User updatedUserWithoutLogin = User.builder().id(addedUser.getId()).email("bigman@ya.ru").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User updatedUserWithoutEmail = User.builder().id(addedUser.getId()).login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User updatedUserWithoutName = User.builder().id(addedUser.getId()).email("bigman@ya.ru").login("BigMan")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User updatedUserWithoutBirthday = User.builder().id(addedUser.getId()).email("bigman@ya.ru").login("BigMan")
                .name("Mannish").build();

        Set<ConstraintViolation<User>> violations = validator.validate(updatedUserEmpty);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<User>> violations2 = validator.validate(updatedUserWithoutLogin);
        Assertions.assertFalse(violations2.isEmpty());
        Set<ConstraintViolation<User>> violations3 = validator.validate(updatedUserWithoutEmail);
        Assertions.assertFalse(violations3.isEmpty());
        controller.updateUser(updatedUserWithoutName);
        Assertions.assertEquals(updatedUserWithoutName.getLogin(), controller.getUsers().get(0).getName());
        Set<ConstraintViolation<User>> violations4 = validator.validate(updatedUserWithoutBirthday);
        Assertions.assertFalse(violations4.isEmpty());
    }
}