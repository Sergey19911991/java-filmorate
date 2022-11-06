package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(inMemoryUserStorage.findAll().contains(user));
    }


    @ParameterizedTest
    @CsvSource({"User,Login, emailemail,1990, 12, 3",
            "User,Log in,emailemail,1990, 12, 3",
            "User,Login, emailemail,2222, 12, 3"})
    public void errorEmail(String name, String login, String email, int year, int month, int day) {
        assertThrows(
                ValidationException.class,
                () -> {
                    {
                        User user = new User();
                        user.setName(name);
                        user.setBirthday(LocalDate.of(year, month, day));
                        user.setLogin(login);
                        user.setEmail(email);
                        inMemoryUserStorage.create(user);
                    }
                });
    }

    @Test
    public void errorEmail1() {
        assertThrows(
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
    public void errorLogin() {
        assertThrows(
                ValidationException.class, () -> {
                    {
                        User user = new User();
                        user.setName("Name");
                        user.setBirthday(LocalDate.of(1990, 12, 3));
                        user.setLogin("");
                        user.setEmail("email@mail.ru");
                        inMemoryUserStorage.create(user);
                    }
                });

    }

}
