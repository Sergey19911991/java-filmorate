package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@RequestMapping("/users")
@Slf4j
@RestController
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping(value = "")
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }


    @PutMapping("")
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }


    @GetMapping("")
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userStorage.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User putFriends(@PathVariable int id, @PathVariable int friendId) {
        return userService.putFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deletFriends(@PathVariable int id, @PathVariable int friendId) {
        return userService.deletFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

}

