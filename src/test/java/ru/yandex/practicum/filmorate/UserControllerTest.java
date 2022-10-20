package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

public class UserControllerTest {
    public InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @Test
    public void createUser() {
        User user = new User();
        user.setId(1);
        user.setName("User");
        user.setBirthday(LocalDate.of(1990, 12, 3));
        user.setLogin("Login");
        user.setEmail("email@mail.ru");
        inMemoryUserStorage.create(user);
        assertEquals(true, inMemoryUserStorage.findAll().contains(user));
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
                        inMemoryUserStorage.create(user);
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
                        inMemoryUserStorage.create(user);
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
                        inMemoryUserStorage.create(user);
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
                        inMemoryUserStorage.create(user);
                    }
                });

    }
}
