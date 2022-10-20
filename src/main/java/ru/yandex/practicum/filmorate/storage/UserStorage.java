package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

@Component
public interface UserStorage {
    User create (@Valid @RequestBody User user);
    User updateUser(@Valid @RequestBody User user);
    Collection<User> findAll();
    User getUser(int id);
}
