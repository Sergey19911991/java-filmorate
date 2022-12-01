package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private int userNumber = 1;


    public User create(@Valid User user) {
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

    public User updateUser(@Valid User user) {
        try {
            if (users.containsKey(user.getId())) {
                validationUser(user);
                users.put(user.getId(), user);
                log.info("Добавлен пользователь {}", user);
            } else {
                log.error("Пользователь {} не существует", user);
                throw new NotFoundException("Такого пользователя не существует");
            }
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return user;
    }

    public Collection<User> findAll() {
        log.error("Список пользователей");
        return users.values();
    }

    public User getUser(int id) {
        if (!users.containsKey(id)) {
            log.error("Пользователь {} не существует", users.get(id));
            throw new NotFoundException("Такого пользователя не существует");
        }
        log.info("Пользователь id = {}", id);
        return users.get(id);
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
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

}
