package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@RestController
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private int userNumber = 1;

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        try {
            user.setId(userNumber);
            validationUser(user);
            users.put(userNumber, user);
            userNumber = userNumber + 1;
            log.info("Добавлен пользователь {}", user);
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return user;
    }


    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        try {
            if (users.containsKey(user.getId())) {
                validationUser(user);
                users.put(user.getId(), user);
                log.info("Добавлен пользователь {}", user);
            } else {
                log.error("Пользователь {} не существует", user);
                throw new ValidationException("Такого пользователя не существует");
            }
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return user;
    }


    @GetMapping("/users")
    public Collection<User> findAll() {
        Collection<User> usersCollection = users.values();
        return usersCollection;
    }

    private void validationUser(User user) {
        if (user.getLogin().isEmpty()) {
            log.error("Ошибка в логине пользователя {}", user);
            throw new ValidationException("Логин не может быть пустым!");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Ошибка в логине пользователя {}", user);
            throw new ValidationException("Логин не может содержать пробел!");
        }
        if (user.getEmail().isEmpty()) {
            log.error("Ошибка в email пользователя {}", user);
            throw new ValidationException("Email не может быть пустым!");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Ошибка в email пользователя {}", user);
            throw new ValidationException("Email должен содержать символ @!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка в дате рождения пользователя {}", user);
            throw new ValidationException("День рождения не может быть в будущем!");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}

