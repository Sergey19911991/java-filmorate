package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@RequestMapping("/users")
@Slf4j
@RestController
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserController(UserDbStorage userDbStorage, UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
        this.userDbStorage = userDbStorage;
    }

    @PostMapping(value = "")
    public User create(@Valid @RequestBody User user) {
        return userDbStorage.create(user);
    }


    @PutMapping("")
    public User updateUser(@Valid @RequestBody User user) {
        return userDbStorage.updateUser(user);
    }


    @GetMapping("")
    public Collection<User> findAll() {
        return userDbStorage.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userDbStorage.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User putFriends(@PathVariable int id, @PathVariable int friendId) {
        return userDbStorage.putFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public int deletFriends(@PathVariable int id, @PathVariable int friendId) {
        return userDbStorage.deletFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable int id) {
        return userDbStorage.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userDbStorage.getCommonFriends(id, otherId);
    }

}

