package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserDbService;

@RequestMapping("/users")
@Slf4j
@RestController
public class UserController {

    private final UserDbService userDbService;
    private final EventService eventService;


    @Autowired
    public UserController(UserDbService userDbService, EventService eventService) {
        this.userDbService = userDbService;
        this.eventService = eventService;
    }

    @PostMapping(value = "")
    public User create(@Valid @RequestBody User user) {
        return userDbService.create(user);
    }


    @PutMapping("")
    public User updateUser(@Valid @RequestBody User user) {
        return userDbService.updateUser(user);
    }


    @GetMapping("")
    public Collection<User> findAll() {
        return userDbService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userDbService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User putFriends(@PathVariable int id, @PathVariable int friendId) {
        return userDbService.putFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public int deletFriends(@PathVariable int id, @PathVariable int friendId) {
        return userDbService.deletFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable int id) {
        return userDbService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userDbService.getCommonFriends(id, otherId);
    }


    @GetMapping("/{id}/feed")
    public Collection<Event> getFeedByUserId(@PathVariable int id) {
        return eventService.getEventByUserId(id);
    }


    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable int userId) {
        userDbService.deleteUserById(userId);
    }


}

