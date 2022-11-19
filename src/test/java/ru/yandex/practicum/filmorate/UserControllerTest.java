package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final UserDbStorage userDbStorage;


    @ParameterizedTest
    @CsvSource({"User,Login, emailemail,1990, 12, 3",
            "User,Log in,emailemail,1990, 12, 3",
            "User,Login, emailemail,2222, 12, 3"})
    public void errorEmailBirthDateLogin(String name, String login, String email, int year, int month, int day) {
        assertThrows(
                ValidationException.class,
                () -> {
                    {
                        userDbStorage.create(User.builder()
                                .email(email)
                                .birthday(LocalDate.of(year, month, day))
                                .name(name)
                                .login(login)
                                .build());
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
                        userDbStorage.create(User.builder()
                                .email("")
                                .birthday(LocalDate.of(1991, 06, 19))
                                .name("Name")
                                .login("login")
                                .build());
                    }
                });
    }

    @Test
    public void errorLogin() {
        assertThrows(
                ValidationException.class, () -> {
                    {
                        userDbStorage.create(User.builder()
                                .email("email@email")
                                .birthday(LocalDate.of(1991, 06, 19))
                                .name("Name")
                                .login("")
                                .build());
                    }
                });

    }

}
