package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {
    private UserController controller;

    public UserControllerTest() {
        controller = new UserController();
    }

    @Test
    void createUser() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        Assertions.assertEquals(1, controller.getUsers().size(), "Неверное количество пользователей.");
        Assertions.assertEquals(user, controller.getUsers().get(0), "Пользователи не совпадают.");
    }

    @Test
    void createUserWithBlankEmailOrWithEmailWithoutAtSign() {
        User user = User.builder().email(" ").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User user2 = User.builder().email("bigmanya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addUser(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.",
                e.getMessage(), "Сообщения об ошибке не совпадают.");

        ValidationException e2 = Assertions.assertThrows(ValidationException.class, () -> controller.addUser(user2));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.",
                e2.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    void createUserWithBlankLogin() {
        User user = User.builder().email("bigman@ya.ru").login(" ").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.",
                e.getMessage(), "Сообщения об ошибке не совпадают.");
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

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addUser(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем.",
                e.getMessage(), "Сообщения об ошибке не совпадают.");
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

        NullPointerException e = Assertions.assertThrows(NullPointerException.class,
                () -> controller.addUser(userEmpty));
        Assertions.assertEquals(NullPointerException.class,
                e.getClass(), "Не совпадает название ошибки.");
        NullPointerException e2 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.addUser(userWithoutLogin));
        Assertions.assertEquals(NullPointerException.class,
                e2.getClass(), "Не совпадает название ошибки.");
        NullPointerException e3 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.addUser(userWithoutEmail));
        Assertions.assertEquals(NullPointerException.class,
                e3.getClass(), "Не совпадает название ошибки.");
        NullPointerException e4 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.addUser(userWithoutName));
        Assertions.assertEquals(NullPointerException.class,
                e4.getClass(), "Не совпадает название ошибки.");
        NullPointerException e5 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.addUser(userWithoutBirthday));
        Assertions.assertEquals(NullPointerException.class,
                e5.getClass(), "Не совпадает название ошибки.");
    }

    @Test
    void updateUser() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        User updatedUser = user.toBuilder().email("smallwoman@google.com").login("SmallWoman").name("Womanish")
                .birthday(LocalDate.of(2004, 12, 12)).build();
        controller.updateUser(updatedUser);

        Assertions.assertEquals(1, controller.getUsers().size(), "Неверное количество пользователей.");
        Assertions.assertEquals(updatedUser, controller.getUsers().get(0), "Пользователи не совпадают.");
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

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.addUser(updatedUser));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.",
                e.getMessage(), "Сообщения об ошибке не совпадают.");

        ValidationException e2 = Assertions.assertThrows(ValidationException.class,
                () -> controller.addUser(updatedUser2));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.",
                e2.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateUserWithBlankLogin() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        User updatedUser = user.toBuilder().email("smallwoman@google.com").login(" ").name("Womanish")
                .birthday(LocalDate.of(2004, 12, 12)).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.addUser(updatedUser));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.",
                e.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateUserWithBlankName() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        User updatedUser = user.toBuilder().email("smallwoman@google.com").login("SmallWoman").name(" ")
                .birthday(LocalDate.of(2004, 12, 12)).build();
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

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.addUser(updatedUser));
        Assertions.assertEquals("Дата рождения не может быть в будущем.",
                e.getMessage(), "Сообщения об ошибке не совпадают.");
    }


    @Test
    void updateUserWithNonexistentId() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        User updatedUser = user.toBuilder().id(-1).build();
        User updatedUser2 = user.toBuilder().id(9999).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateUser(updatedUser));
        Assertions.assertEquals("Невозможно обновить пользователя. id: " + updatedUser.getId()
                        + " не существует.", e.getMessage(), "Сообщения об ошибке не совпадают.");

        ValidationException e2 = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateUser(updatedUser2));
        Assertions.assertEquals("Невозможно обновить пользователя. id: " + updatedUser2.getId()
                + " не существует.", e2.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateUserWithEmptyUserOrWithoutLoginOrEmailOrNameOrBirthday() {
        User user = User.builder().email("bigman@ya.ru").login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        controller.addUser(user);

        User updatedUserEmpty = User.builder().id(user.getId()).build();
        User updatedUserWithoutLogin = User.builder().id(user.getId()).email("bigman@ya.ru").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User updatedUserWithoutEmail = User.builder().id(user.getId()).login("BigMan").name("Mannish")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User updatedUserWithoutName = User.builder().id(user.getId()).email("bigman@ya.ru").login("BigMan")
                .birthday(LocalDate.of(1994, 1, 1)).build();
        User updatedUserWithoutBirthday = User.builder().id(user.getId()).email("bigman@ya.ru").login("BigMan")
                .name("Mannish").build();

        NullPointerException e = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateUser(updatedUserEmpty));
        Assertions.assertEquals(NullPointerException.class,
                e.getClass(), "Не совпадает название ошибки.");
        NullPointerException e2 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateUser(updatedUserWithoutLogin));
        Assertions.assertEquals(NullPointerException.class,
                e2.getClass(), "Не совпадает название ошибки.");
        NullPointerException e3 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateUser(updatedUserWithoutEmail));
        Assertions.assertEquals(NullPointerException.class,
                e3.getClass(), "Не совпадает название ошибки.");
        NullPointerException e4 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateUser(updatedUserWithoutName));
        Assertions.assertEquals(NullPointerException.class,
                e4.getClass(), "Не совпадает название ошибки.");
        NullPointerException e5 = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateUser(updatedUserWithoutBirthday));
        Assertions.assertEquals(NullPointerException.class,
                e5.getClass(), "Не совпадает название ошибки.");
    }
}