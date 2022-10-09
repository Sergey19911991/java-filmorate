package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.UserController;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.function.Executable;

public class UserControllerTest {
    UserController userController = new UserController();

    @Test
    public void createUser() {
        User user = new User();
        user.setId(1);
        user.setName("User");
        user.setBirthday(LocalDate.of(1990, 12, 3));
        user.setLogin("Login");
        user.setEmail("email@mail.ru");
        userController.create(user);
        assertEquals(user, userController.findAll().get(0));
    }


    @Test
    public void errorName() {
        User user = new User();
        user.setBirthday(LocalDate.of(1990, 12, 3));
        user.setLogin("Login");
        user.setEmail("email@mail.ru");
        userController.create(user);
        assertEquals(user.getLogin(), userController.findAll().get(0).getName());
    }


    @Test
    public void errorEmail() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        User user = new User();
                        user.setName("Name");
                        user.setBirthday(LocalDate.of(1990, 12, 3));
                        user.setLogin("Login");
                        user.setEmail("");
                        userController.create(user);
                    }
                });
    }

    @Test
    public void errorEmail1() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        User user = new User();
                        user.setName("Name");
                        user.setBirthday(LocalDate.of(1990, 12, 3));
                        user.setLogin("Login");
                        user.setEmail("emailmail.ru");
                        userController.create(user);
                    }
                });
    }

    @Test
    public void errorLogin() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        User user = new User();
                        user.setName("Name");
                        user.setBirthday(LocalDate.of(1990, 12, 3));
                        user.setLogin("Log in");
                        user.setEmail("email@mail.ru");
                        userController.create(user);
                    }
                });

    }

    @Test
    public void errorBirthday() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        User user = new User();
                        user.setName("Name");
                        user.setBirthday(LocalDate.of(2222, 12, 3));
                        user.setLogin("Login");
                        user.setEmail("email@mail.ru");
                        userController.create(user);
                    }
                });

    }
}
