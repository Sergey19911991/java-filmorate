package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.UserController;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
        user.setId(1);
        user.setName("");
        user.setBirthday(LocalDate.of(1990, 12, 3));
        user.setLogin("Login");
        user.setEmail("email@mail.ru");
        userController.create(user);
        assertEquals(user.getLogin(), userController.findAll().get(0).getName());
    }


    @Test
    public void errorEmail() {
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setBirthday(LocalDate.of(1990, 12, 3));
        user.setLogin("Login");
        user.setEmail("");
        userController.create(user);
        assertEquals(0,userController.findAll().size());
    }

    @Test
    public void errorEmail1() {
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setBirthday(LocalDate.of(1990, 12, 3));
        user.setLogin("Login");
        user.setEmail("emailmail.ru");
        userController.create(user);
        assertEquals(0,userController.findAll().size());
    }

    @Test
    public void errorLogin() {
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setBirthday(LocalDate.of(1990, 12, 3));
        user.setLogin("Log in");
        user.setEmail("email@mail.ru");
        userController.create(user);
        assertEquals(0,userController.findAll().size());
    }

    @Test
    public void errorBirthday() {
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setBirthday(LocalDate.of(2222, 12, 3));
        user.setLogin("Login");
        user.setEmail("email@mail.ru");
        userController.create(user);
        assertEquals(0,userController.findAll().size());
    }
}
