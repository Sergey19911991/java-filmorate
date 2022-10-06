package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.model.User;

@RestController
public class UserController {
    private List<User> users = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/user")
    public User create(@RequestBody User user) {

        try {
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
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            int k = 0;
            if (!users.isEmpty()) {
                for (int i = 0; i < users.size(); i++) {
                    if (user.getId() == users.get(i).getId()) {
                        users.remove(i);
                        users.add(user);
                        log.info("Добавлен пользователь {}", user);
                        k++;
                        break;
                    }
                }
                if (k == 0) {
                    users.add(user);
                    log.info("Добавлен пользователь {}", user);
                }
            } else {
                users.add(user);
                log.info("Добавлен пользователь {}", user);
            }
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return users;
    }
}

