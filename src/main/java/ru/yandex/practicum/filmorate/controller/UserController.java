package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.model.User;

@RestController
public class UserController {
    private List<User> users = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int n = 1;

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        try {
            user.setId(n);
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
            n = n + 1;
            users.add(user);
            log.info("Добавлен пользователь {}", user);
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return user;
    }


    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        try {
            int k = 0;
            for (int i = 0; i < users.size(); i++) {
                if (user.getId() == users.get(i).getId()) {
                    int userId = user.getId();
                    users.remove(i);
                    create(user);
                    users.get(i).setId(userId);
                    log.info("Добавлен пользователь {}", user);
                    k++;
                    break;
                }
            }
            if (k == 0) {
                log.error("Пользователь {} не существует", user);
                throw new ValidationException("Такого пользователя не существует");
            }
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return user;
    }


    @GetMapping("/users")
    public List<User> findAll() {
        return users;
    }
}

